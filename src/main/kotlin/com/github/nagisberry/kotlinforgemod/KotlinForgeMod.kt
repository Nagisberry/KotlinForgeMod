package com.github.nagisberry.kotlinforgemod

import com.mojang.logging.LogUtils
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.event.server.ServerStartingEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.InterModComms
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import java.util.stream.Collectors

@Mod("kotlinforgemod")
class KotlinForgeMod {

    companion object {
        // Directly reference a slf4j logger
        private val LOGGER = LogUtils.getLogger()
    }

    init {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().modEventBus.addListener(::setup)
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().modEventBus.addListener(::enqueueIMC)
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().modEventBus.addListener(::processIMC)

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this)
    }

    private fun setup(event: FMLCommonSetupEvent) {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT")
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.registryName)
    }

    private fun enqueueIMC(event: InterModEnqueueEvent) {
        // Some example code to dispatch IMC to another mod
        InterModComms.sendTo("examplemod", "helloworld") {
            LOGGER.info("Hello world from the MDK")
            "Hello world"
        }
    }

    private fun processIMC(event: InterModProcessEvent) {
        // Some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.imcStream.map { it.messageSupplier().get() }.collect(Collectors.toList()))
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    fun onServerStarting(event: ServerStartingEvent?) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting")
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
    object RegistryEvents {
        @SubscribeEvent
        fun onBlocksRegistry(blockRegistryEvent: RegistryEvent.Register<Block>) {
            // Register a new block here
            LOGGER.info("HELLO from Register Block")
        }
    }
}