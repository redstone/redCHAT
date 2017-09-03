/*
 * Copyright 2014 ZerothAngel <zerothangel@tyrannyofheaven.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.redstoneore.chat.util.uuid;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.common.base.Charsets;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.Lists;

public class MojangUuidResolver implements UuidResolver {

    private static final String AGENT = "minecraft";

    private static final UuidDisplayName NULL_UDN = new UuidDisplayName(UUID.randomUUID(), "NOT FOUND");

    private static MojangUuidResolver instance = null;
    public static MojangUuidResolver get() {
    		if (instance == null) {
    			instance = new MojangUuidResolver(100, 12, TimeUnit.HOURS);
    		}
    		return instance;
    }
    private final Cache<String, UuidDisplayName> cache;

    public MojangUuidResolver(int cacheMaxSize, long cacheTtl, TimeUnit cacheTtlUnits) {
        cache = CacheBuilder.newBuilder()
                .maximumSize(cacheMaxSize)
                .expireAfterWrite(cacheTtl, cacheTtlUnits)
                .build(new CacheLoader<String, UuidDisplayName>() {
                    @Override
                    public UuidDisplayName load(String key) throws Exception {
                        UuidDisplayName udn = _resolve(key);
                        return udn != null ? udn : NULL_UDN; // Doesn't like nulls, so we use a marker object instead
                    }
                });
    }

    @Override
    public UuidDisplayName resolve(String username) {
        if (username.trim() == "")
            throw new IllegalArgumentException("username must have a value");

        try {
            UuidDisplayName udn = cache.getIfPresent(username.toLowerCase());
            return udn != NULL_UDN ? udn : null;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public UuidDisplayName resolve(String username, boolean cacheOnly) {
        if (username.trim() == "")
            throw new IllegalArgumentException("username must have a value");

        if (cacheOnly) {
            UuidDisplayName udn = cache.asMap().get(username.toLowerCase());
            if (udn == null) return null;
            return udn != NULL_UDN ? udn : null; // NB Can't tell between "not cached" and "maps to null"
        }
        else return resolve(username); // Same as normal version
    }

    @Override
    public Map<String, UuidDisplayName> resolve(Collection<String> usernames) throws Exception {
        if (usernames == null)
            throw new IllegalArgumentException("usernames cannot be null");

        Map<String, UuidDisplayName> result = new LinkedHashMap<>();

        final int BATCH_SIZE = 97; // Should be <= Mojang's AccountsClient's PROFILES_PER_REQUEST (100)

        for (List<String> sublist : Lists.partition(new ArrayList<>(usernames), BATCH_SIZE)) {
            List<Profile> searchResult = searchProfiles(sublist);
            for (Profile profile : searchResult) {
                String username = profile.getName();
                UUID uuid = UuidUtils.uncanonicalizeUuid(profile.getId());
                result.put(username.toLowerCase(), new UuidDisplayName(uuid, username));
            }
        }

        return result;
    }

    @Override
    public void preload(String username, UUID uuid) {
        if (username.trim() == "")
            throw new IllegalArgumentException("username must have a value");
        if (uuid == null)
            throw new IllegalArgumentException("uuid cannot be null");

        cache.asMap().put(username.toLowerCase(), new UuidDisplayName(uuid, username));
    }

    @Override
    public void invalidate(String username) {
        if (username.trim() == "")
            throw new IllegalArgumentException("username must have a value");

        cache.invalidate(username.toLowerCase());
    }

    @Override
    public void invalidateAll() {
        cache.invalidateAll();
    }

    private UuidDisplayName _resolve(String username) throws IOException, ParseException {
        if (username.trim() == "")
            throw new IllegalArgumentException("username must have a value");

        List<Profile> result = searchProfiles(Collections.singletonList(username));

        if (result.size() < 1) return null;

        // TODO what to do if there are >1?
        Profile p = result.get(0);

        String uuidString = p.getId();
        UUID uuid;
        try {
            uuid = UuidUtils.uncanonicalizeUuid(uuidString);
        }
        catch (IllegalArgumentException e) {
            return null;
        }

        String displayName = (p.getName() != null && p.getName().trim() != "") ? p.getName() : username;

        return new UuidDisplayName(uuid, displayName);
    }

    private List<Profile> searchProfiles(List<String> usernames) throws IOException, ParseException {
        String body = JSONValue.toJSONString(usernames);

        URL url = new URL("https://api.mojang.com/profiles/" + AGENT);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(15000);
        
        DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
        try {
            writer.write(body.getBytes(Charsets.UTF_8));
            writer.flush();
        }
        finally {
            writer.close();
        }

        Reader reader = new InputStreamReader(connection.getInputStream());
        JSONArray profiles;
        try {
            JSONParser parser = new JSONParser(); // NB Not thread safe
            profiles = (JSONArray)parser.parse(reader);
        }
        finally {
            reader.close();
        }
        
        return convertResponse(profiles);
    }

    private List<Profile> convertResponse(JSONArray profiles) {
        List<Profile> result = new ArrayList<>();
        for (Object obj : profiles) {
            JSONObject jsonProfile = (JSONObject)obj;
            String id = (String)jsonProfile.get("id");
            String name = (String)jsonProfile.get("name");
            result.add(new Profile(id, name));
        }
        return result;
    }

    private static class Profile {
        
        private final String id;
        
        private final String name;

        private Profile(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

    }

}