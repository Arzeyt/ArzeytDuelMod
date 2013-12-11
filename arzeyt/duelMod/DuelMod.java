package arzeyt.duelMod;

import java.util.logging.Logger;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;

@Mod(modid="DuelMod", name="DuelMod", version="0.1")
public class DuelMod {
	@Mod.Instance("DuelMod")
	public static DuelMod instance;
	public static BattleListener battleListener;
	public static Logger myLogger;
	
	@EventHandler
	public void preInit(FMLInitializationEvent event){
		myLogger = Logger.getLogger("DuelMod");
		myLogger.setParent(FMLLog.getLogger());
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event){
		this.battleListener = new BattleListener();
		MinecraftForge.EVENT_BUS.register(battleListener);
	}
	
	
	
	
}
