package arzeyt.duelMod;

import java.util.ArrayList;
import java.util.List;

import javax.jws.Oneway;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class BattleListener{

	private EntityPlayer player;
	private EntityPlayer attacker;
	private String weapon;
	
	@ForgeSubscribe
	public void onPlayerInteract(LivingHurtEvent event){
		if(event.entity instanceof EntityPlayer && (event.source.getSourceOfDamage() instanceof EntityPlayer
				|| event.source.getEntity() instanceof EntityPlayer)){
			player = (EntityPlayer) event.entity;
			attacker = (EntityPlayer) event.source.getEntity();
			weapon = attacker.getHeldItem().getDisplayName();
			
			if(player.getDisplayName().equals(attacker.getDisplayName())){
				return;
			}
			
			if(player.getHealth() - event.ammount < 1){
				sendMessageToAll(player.getDisplayName()+" was murdered by "+attacker.getDisplayName()+"'s "+weapon+"!");
				handleEvent(player, attacker, event);
			}
		}
	}


	private void sendMessageToAll(String msg) {
		ArrayList<EntityPlayer> players = (ArrayList<EntityPlayer>) MinecraftServer.getServer().getConfigurationManager().playerEntityList;
		for(EntityPlayer player : players){
			sendMessagToPlayer(player, msg);
		}
	}


	private void handleEvent(EntityPlayer player, EntityPlayer attacker,
			LivingHurtEvent event) {
		
		if(event.isCancelable()){
			event.setCanceled(true);
			

			teleToSpawn(player);
			player.setHealth(20);
			
		}
	}

	private void teleToSpawn(EntityPlayer player) {
		int[] spawnLocation = getSpawnLocation(player);
		
		player.setPosition(spawnLocation[0], spawnLocation[1], spawnLocation[2]);
		int c = (int)player.posY;
		while(c < 256){
			if(player.worldObj.isAirBlock((int)player.posX, c, (int)player.posZ)==false){
				c++;
			}else{
				player.posY = c;
				break;
			}
		}
	}

	private int[] getSpawnLocation(EntityPlayer player) {
		int[] positions = new int[3];
		int posx, posy, posz;
		
		int dim = player.worldObj.provider.dimensionId;
		if(player.getBedLocation(dim)!=null){ //bed location for dim 1
			positions[0] = player.getBedLocation(dim).posX;
			positions[1] = player.getBedLocation(dim).posY;
			positions[2] = player.getBedLocation(dim).posZ;
		}else{ //worldSpawn;
			positions[0] = player.worldObj.getSpawnPoint().posX;
			positions[1] = player.worldObj.getSpawnPoint().posY;
			positions[2] = player.worldObj.getSpawnPoint().posZ;
		}
		return positions;
	}

	public void sendMessagToPlayer(EntityPlayer player, String msg){
		player.sendChatToPlayer(new ChatMessageComponent().createFromText(msg));
	}
}
