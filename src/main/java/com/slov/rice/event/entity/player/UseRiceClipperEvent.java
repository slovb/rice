package com.slov.rice.event.entity.player;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemUseContext;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event.HasResult;

@Cancelable
@HasResult
public class UseRiceClipperEvent extends PlayerEvent {

	private final ItemUseContext context;
	
	public UseRiceClipperEvent(ItemUseContext context) {
		super(context.getPlayer());
		this.context = context;
	}

	@Nonnull
	public ItemUseContext getContext() {
		return context;
	}
}
