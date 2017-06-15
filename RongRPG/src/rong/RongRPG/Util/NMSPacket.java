package rong.RongRPG.Util;

import io.netty.buffer.Unpooled;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_10_R1.EnumHand;
import net.minecraft.server.v1_10_R1.IChatBaseComponent;
import net.minecraft.server.v1_10_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_10_R1.NBTTagCompound;
import net.minecraft.server.v1_10_R1.NBTTagList;
import net.minecraft.server.v1_10_R1.NBTTagString;
import net.minecraft.server.v1_10_R1.PacketDataSerializer;
import net.minecraft.server.v1_10_R1.PacketPlayOutChat;
import net.minecraft.server.v1_10_R1.PacketPlayOutCustomPayload;
import net.minecraft.server.v1_10_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_10_R1.PacketPlayOutTitle.EnumTitleAction;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NMSPacket 
{
	public static void sendActionBar(Player player, String message)
	{
		if(message != null)
		{
			IChatBaseComponent icbc = ChatSerializer.a("{\"text\":\"" + message + "\"}");
			PacketPlayOutChat ppoc = new PacketPlayOutChat(icbc, (byte)2);
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(ppoc);
		}
	}
	
	public static void sendTitle(Player player, int fadeIn, int stay, int fadeOut, String title, String subtitle) 
	{
		if (title != null) 
		{
			PacketPlayOutTitle ppot = new PacketPlayOutTitle(EnumTitleAction.TITLE, ChatSerializer.a("{\"text\":\"" + title + "\"}"), fadeIn, stay, fadeOut);
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(ppot);
		}
		
		if (subtitle != null) 
		{
			PacketPlayOutTitle ppot = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, ChatSerializer.a("{\"text\":\"" + subtitle + "\"}"), fadeIn, stay, fadeOut);
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(ppot);
		}
	}
	
	public static TextComponent ChatButton(String text, String str, String command)
	{
		TextComponent t = new TextComponent(text);

		t.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(str)}));
		t.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));

		return t;
	}
	
	public static TextComponent addTextComponentClickEvent(TextComponent text, String command)
	{
		text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
		
		return text;
	}
	
	public static TextComponent addTextComponentHoverEvent(TextComponent text, String str)
	{
		text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(str)}));
		
		return text;
	}
	
	public static TextComponent ItemConvertTextComponent(ItemStack item)
	{
		TextComponent text = new TextComponent(item.getItemMeta().getDisplayName());
		net.minecraft.server.v1_10_R1.ItemStack is = CraftItemStack.asNMSCopy(item);
		NBTTagCompound tag = is.save(new NBTTagCompound());
		String itemJson = tag.toString();
		
		text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new BaseComponent[]{new TextComponent(itemJson)}));
		
		return text;
	}
	
	public static void openBook(Player player, TextComponent... text)
	{
		ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
		net.minecraft.server.v1_10_R1.ItemStack nmsbook = CraftItemStack.asNMSCopy(book);
		NBTTagCompound tag = new NBTTagCompound();
		NBTTagList taglist = new NBTTagList();

		for (TextComponent t : text)
		{
			taglist.add(new NBTTagString(ComponentSerializer.toString(t)));
		}

		tag.set("pages", taglist);
		nmsbook.setTag(tag);

		book = CraftItemStack.asBukkitCopy(nmsbook);

		int slot = player.getInventory().getHeldItemSlot();
		ItemStack old = player.getInventory().getItem(slot);
		player.getInventory().setItem(slot, book);
		PacketDataSerializer packetdataserializer = new PacketDataSerializer(Unpooled.buffer());
		packetdataserializer.a(EnumHand.MAIN_HAND);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutCustomPayload("MC|BOpen", packetdataserializer));
		player.getInventory().setItem(slot, old);
	}
	
//	public static void openBook(Player player, List<TextComponent> page)
//	{
//		ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
//		net.minecraft.server.v1_10_R1.ItemStack nmsbook = CraftItemStack.asNMSCopy(book);
//		NBTTagCompound tag = new NBTTagCompound();
//		NBTTagList taglist = new NBTTagList();
//
//		for (TextComponent t : page)
//		{
//			taglist.add(new NBTTagString(ComponentSerializer.toString(t)));
//		}
//
//		tag.set("pages", taglist);
//		nmsbook.setTag(tag);
//
//		book = CraftItemStack.asBukkitCopy(nmsbook);
//
//		int slot = player.getInventory().getHeldItemSlot();
//		ItemStack old = player.getInventory().getItem(slot);
//		player.getInventory().setItem(slot, book);
//		PacketDataSerializer packetdataserializer = new PacketDataSerializer(Unpooled.buffer());
//		packetdataserializer.a(EnumHand.MAIN_HAND);
//		((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutCustomPayload("MC|BOpen", packetdataserializer));
//		player.getInventory().setItem(slot, old);
//	}
	
	public static Class<?> getNmsClass(String nmsClassName) throws ClassNotFoundException
	{
		return Class.forName("net.minecraft.server." + getServerVersion() + "." + nmsClassName);
	}

	public static String getServerVersion()
	{
		return Bukkit.getServer().getClass().getPackage().getName().substring(23);
	}

	public static void sendJson(Player player, String msg)
	{
		String version = getServerVersion();
		try{
			Object icbc = getNmsClass(version.equalsIgnoreCase("v1_8_R1") ? "ChatSerializer" : "IChatBaseComponent$ChatSerializer").getMethod("a", new Class[] { String.class }).invoke(null, new Object[] {msg});
			Object ppoc = getNmsClass("PacketPlayOutChat").getConstructor(new Class[] { getNmsClass("IChatBaseComponent"), Byte.TYPE }).newInstance(new Object[] { icbc, Byte.valueOf("1") });      
			Object nmsp = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);     
			Object pcon = nmsp.getClass().getField("playerConnection").get(nmsp); 
			pcon.getClass().getMethod("sendPacket", new Class[] { getNmsClass("Packet") }).invoke(pcon, new Object[] { ppoc });
		}catch (Exception e){
			Bukkit.broadcastMessage("Error of " + e.getMessage());
		}
	}
}
