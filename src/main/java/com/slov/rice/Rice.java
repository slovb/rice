package com.slov.rice;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.slov.rice.init.ModBlocks;
import com.slov.rice.init.ModItems;

// The value here should match an entry in the META-INF/mods.toml file.
@Mod(Rice.MODID)
public class Rice
{
	public static final String MODID = "rice";
	
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public Rice() {
    	IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
    	MinecraftForge.EVENT_BUS.register(this);
    	
    	ModBlocks.BLOCKS.register(bus);
    	ModItems.ITEMS.register(bus);
    }
}
