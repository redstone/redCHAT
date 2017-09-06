package net.redstoneore.chat;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * An RLocation is async safe way to interact with locations
 */
public class RLocation {

	// -------------------------------------------------- //
	// STATIC METHODS
	// -------------------------------------------------- //
	
	/**
	 * This is not async safe.<br>
	 * Store the location (by fetching world and chunk) into an RLocation.
	 * @param location
	 * @return
	 */
	public static RLocation create(Location location) {
		return new RLocation(
			location.getWorld().getName(),
			location.getWorld().getUID(), 
			location.getChunk().getX(), 
			location.getChunk().getZ(),
			location.getX(),
			location.getY(),
			location.getZ(),
			location.getYaw(),
			location.getPitch()
		);	
	}
	
	// -------------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------------- //
	
	public RLocation(String worldName, UUID world, int chunkX, int chunkZ, double locationX, double locationY, double locationZ, float yaw, float pitch) {
		this.worldName = worldName;
		this.world = world;
		
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		
		this.locationX = locationX;
		this.locationY = locationY;
		this.locationZ = locationZ;
		
		this.yaw = yaw;
		this.pitch = pitch;
	}
	
	// -------------------------------------------------- //
	// FIELDS
	// -------------------------------------------------- //
	
	private final String worldName;
	private final UUID world;
	
	private final int chunkX;
	private final int chunkZ;
	
	private final double locationX;
	private final double locationY;
	private final double locationZ;
	
	private final float yaw;
	private final float pitch;
	
	// -------------------------------------------------- //
	// METHODS
	// -------------------------------------------------- //
	
	public String getWorldName() {
		return this.worldName;
	}
	
	public UUID getWorldUID() {
		return this.world;
	}
	
	public int getChunkX() {
		return this.chunkX;
	}
	
	public int getChunkZ() {
		return this.chunkZ;
	}
	
	public double getLocationX() {
		return this.locationX;
	}
	
	public double getLocationY() {
		return this.locationY;
	}
	
	public double getLocationZ() {
		return this.locationZ;
	}
		
	public float getLocationYaw() {
		return this.yaw;
	}
	
	public float getLocationPitch() {
		return this.pitch;
	}
	
	public double distance(RLocation location) {
		return Math.sqrt(this.distanceSquared(location));
	}
	
	public double distanceSquared(RLocation location) {
		double distanceX = this.getLocationX() - location.getLocationX();
		double distanceY = this.getLocationY() - location.getLocationY();
		double distanceZ = this.getLocationZ() - location.getLocationZ();
		
		return (distanceX * distanceX) + (distanceY * distanceY) + (distanceZ * distanceZ);
	}
	
	/**
	 * Do not call this method async. Unsure? Ensure {@link Bukkit#isPrimaryThread()} is true.
	 * @return {@link BukkitAPI}
	 */
	public BukkitAPI bukkit() {
		return new BukkitAPI(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		
		if (!(obj instanceof RLocation)) return false;
		
		RLocation other = (RLocation) obj;
		
		return (
			other.getChunkX() == this.getChunkX() &&
			other.getChunkZ() == this.getChunkZ() &&
			other.getLocationX() == this.getLocationX() &&
			other.getLocationY() == this.getLocationY() &&
			other.getLocationZ() == this.getLocationZ() &&
			other.getLocationYaw() == this.getLocationYaw() &&
			other.getLocationPitch() == this.getLocationPitch() &&
			other.getWorldUID() == this.getWorldUID()
		);
	}

	// -------------------------------------------------- //
	// CLASS
	// -------------------------------------------------- //
	
	/**
	 * This class provides sync-only methods for accessing the Bukkit API.
	 */
	public class BukkitAPI {
		
		// -------------------------------------------------- //
		// CONSTRUCT
		// -------------------------------------------------- //
		
		public BukkitAPI(RLocation location) {
			this.location = location;
		}
		
		// -------------------------------------------------- //
		// FIELDS
		// -------------------------------------------------- //
		
		private RLocation location;
		
		// -------------------------------------------------- //
		// METHODS
		// -------------------------------------------------- //
		
		/**
		 * Get the {@link Chunk}<br>
		 * <b>WARNING</b>: Accesses Bukkit API
		 * @return {@link Chunk}
		 */
		public Chunk getChunk() {
			return this.getWorld().getChunkAt(this.location.getChunkX(), this.location.getChunkZ());
		}
		
		/**
		 * Get the {@link Location}<br>
		 * <b>WARNING</b>: Accesses Bukkit API
		 * @return {@link Location}
		 */
		public Location getLocation() {
			return new Location(this.getWorld(), location.getLocationX(), location.getLocationY(), location.getLocationZ(), this.location.getLocationYaw(), this.location.getLocationPitch());
		}
		
		/**
		 * Get the {@link World}<br>
		 * <b>WARNING</b>: Accesses Bukkit API
		 * @return {@link World}
		 */
		public World getWorld() {
			return Bukkit.getWorld(this.location.getWorldUID());
		}
	}
}
