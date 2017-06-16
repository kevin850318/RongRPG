package rong.RongRPG.Data;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import rong.RongRPG.RpgStorage;
import rong.RongRPG.Util.RpgUtil;

public class EquipmentItem
{
	private ItemStack Item;
	private QualityType Quality;
	private EquipmentType Type;
	private List<String> Lore;
	private EnumMap<Slot, Integer> SlotMap = new EnumMap<>(Slot.class);
	private HashMap<Integer, EquipmentAttribute> AttributeMap = new HashMap<>();
	private boolean canWear = false;
	private boolean canGrinded = false;
	
	public EquipmentItem(ItemStack is)
	{
		this.Item = is;
		this.Lore = is.getItemMeta().getLore();
	}
	
	public void loadQualityType()
	{
		String l = ChatColor.stripColor(this.Lore.get(1));
		
		if(l.startsWith("�~��"))
		{
			String[] split = l.split(" ");
			
			if(split.length >= 2)
			{
				String typeName = split[1];
				this.Quality = QualityType.getQualityType(typeName);
				return;
			}	
		}
	}
	
	public void loadEquipmentType()
	{
		String l = ChatColor.stripColor(this.Lore.get(2));
		
		if(l.startsWith("���O"))
		{
			String[] split = l.split(" ");
			
			if(split.length >= 2)
			{
				String typeName = split[1];
				this.Type = EquipmentType.getEquipmentType(typeName);
				this.canWear = true;
			}
		}
	}
		
	public void loadEquipmentContent(PlayerData pData)
	{
		if(this.Quality == null)
		{
			loadQualityType();
		}
		
		if(this.Type == null)
		{
			loadEquipmentType();
		}
		
		for(int i = 2; i < this.Lore.size(); i ++)
		{
			String l = ChatColor.stripColor(this.Lore.get(i));

			if(l.equals("���򥻼ƭȡ�") || l.equals("�����[�ƭȡ�"))
			{	
				for(int j = i + 1; j < this.Lore.size(); j ++)
				{
					String lore2 = ChatColor.stripColor(this.Lore.get(j));
					String[] split = lore2.split(" ");
					
					if(split.length >= 2)
					{
						String attName = split[0];
						String attValue = split[1];
						
						if(l.equals("���򥻼ƭȡ�"))
						{
							this.SlotMap.put(Slot.BASIC_ATTRIBUTE, i);
						}
						else
						{
							attName = split[1];
							attValue = split[0];

							this.SlotMap.put(Slot.ATTITION, i);
						}
						
						EquipmentAttribute att = EquipmentAttribute.transformToEquipmentAttribute(attName);
						
						if(att != null)
						{
							att.setAttributeVault(attValue);

							this.AttributeMap.put(j, att);	
							
							i = j;
							continue;
						}
					}
					break;
				}
			}
			else if(l.equals("���˳ƭ��"))
			{
				for(int j = i + 1; j < this.Lore.size(); j ++)
				{
					String lore2 = ChatColor.stripColor(this.Lore.get(j));
					String[] split = lore2.split(" ");

					if(split.length >= 2)
					{
						String limitName = split[0];
						EquipmentLimit limit = EquipmentLimit.transformToEquipmentLimit(limitName);
						
						if(limit != null)
						{
							int att = pData.getInt(limit.name());
							int value = Integer.parseInt(split[1]);
							
							i = j;
							
							if(att < value)
							{
								this.canWear = false;
								break;
							}
							continue;
						}
					}
					break;
				}
			}
			else if(l.equals("������i"))
			{
				this.canGrinded = true;
				this.SlotMap.put(Slot.ATTITION, i);
			}
		}
	}
	
	public ItemStack getUpgradeItem()
	{
		if(this.SlotMap.containsKey(Slot.BASIC_ATTRIBUTE))
		{
			for(int i = this.SlotMap.get(Slot.BASIC_ATTRIBUTE); i < this.Lore.size(); i++)
			{
				if(this.AttributeMap.containsKey(i))
				{
					EquipmentAttribute ea = this.AttributeMap.get(i);
					int upValue = (int) (ea.getAttributeValue() *0.1);
					int newValue = upValue + ea.getAttributeValue();
					
					this.Lore.set(i, "��f" + ea.getAttributeName() + " +" + newValue);
					
					ItemMeta im = this.Item.getItemMeta();
					im.setLore(this.Lore);
					this.Item.setItemMeta(im);
					continue;
				}
				break;
			}
		}
		return this.Item;
	}
			
	public ItemStack getIdentifiedItem()
	{
		if(this.Quality == null)
		{
			for(QualityType type : QualityType.values())
			{
				if(RpgStorage.random.nextInt(101) <= type.getAppearRate())
				{
					this.Lore.set(1, "��f�~�衱f " + type.getColorString() + type.getName());
					this.Lore.add(type.getColorString() + "������i");
					
					ItemMeta im = this.Item.getItemMeta();
					im.setLore(this.Lore);
					this.Item.setItemMeta(im);
					return this.Item;
				}
			}
		}
		
		return this.Item;
	}
	
	public ItemStack getGridedItem()
	{
		if(this.canGrinded)
		{
			if(this.Quality != null)
			{
				int line = this.SlotMap.get(Slot.ATTITION);
				
				this.Lore.set(line, "��f�����[�ƭȡ�");
				
				for(int i = 0; i < this.Quality.getAmount(); i ++)
				{
					this.Lore.add(line + i + 1, getRandomAttribute());
				}
				
				ItemMeta im = this.Item.getItemMeta();
				im.setLore(this.Lore);
				this.Item.setItemMeta(im);
			}
		}
		
		return this.Item;
	}
	
	public String getRandomAttribute()
	{
		EquipmentAttribute[] ea = EquipmentAttribute.values();
		int i = RpgStorage.random.nextInt(ea.length);
		String lore = ea[i].getAttributeName();
		i = RpgStorage.random.nextInt(10) + 1;
		lore = "��f+" + i + "% " + lore;
		
		return lore;
	}
	
	public ItemStack getItem()
	{
		return this.Item;
	}
	
	public QualityType getQualityType()
	{
		return this.Quality;
	}
	
	public EquipmentType getEquipmentType()
	{
		return this.Type;
	}
		
	public HashMap<Integer, EquipmentAttribute> getAttributeMap()
	{
		return this.AttributeMap;
	}
	
	public boolean canWear()
	{
		return this.canWear;
	}
	
	public boolean canGrinded()
	{
		return this.canGrinded;
	}
	
	public enum Slot
	{
		MAIN_ATTRIBUTE, BASIC_ATTRIBUTE, ATTITION;
	}
	
	public enum EquipmentType
	{
		WEAPON("�Z��", 45, RpgUtil.setItem(Material.IRON_SWORD, 1, 0, "��f�Z��", null, null, false)),
		HELMET("�Y��", 1, RpgUtil.setItem(Material.IRON_HELMET, 1, 0, "��f�Y��", null, null, false)),
		CHESTPLATE("����", 10, RpgUtil.setItem(Material.IRON_CHESTPLATE, 1, 0, "��f����", null, null, false)),
		GLOVES("�@��", 20, RpgUtil.setItem(Material.IRON_HOE, 1, 103, "��f�@��", null, new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE}, true)),
		LEGGINGS("�L��", 19, RpgUtil.setItem(Material.IRON_LEGGINGS, 1, 0, "��f�L��", null, null, false)),
		BOOTS("�c", 28, RpgUtil.setItem(Material.IRON_BOOTS, 1, 0, "��f�c", null, null, false)),
		WING("�ͻH", 11, RpgUtil.setItem(Material.IRON_HOE, 1, 106, "��f�ͻH", null, new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE}, true)),
		EARRING("����", 0, RpgUtil.setItem(Material.IRON_HOE, 1, 107, "��f����", null, new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE}, true)),
		NECKLACE("����", 9, RpgUtil.setItem(Material.LEASH, 1, 0, "��f����", null, null, false)),
		RING("�٫�", 18, RpgUtil.setItem(Material.IRON_HOE, 1, 109, "��f�٫�", null, new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE}, true)),
		;

		private String TypeName;
		private int EquipmentSlot;
		private ItemStack ItemIcon;
		
		EquipmentType(String name, int slot, ItemStack icon)
		{
			this.TypeName = name;
			this.EquipmentSlot = slot;
			this.ItemIcon = icon;
		}
		
		public String getTypeName()
		{
			return this.TypeName;
		}
		
		public int getEquipmentSlot()
		{
			return this.EquipmentSlot;
		}
		
		public ItemStack getItemIcon()
		{
			return this.ItemIcon;
		}
		
		public static EquipmentType getEquipmentType(String str)
		{
			for(EquipmentType type : EquipmentType.values())
			{
				if(type.TypeName.equals(str))
				{
					return type;
				}
			}
			
			return null;
		}
		
		public static EquipmentType getEquipmentType(int slot)
		{
			for(EquipmentType type : EquipmentType.values())
			{
				if(type.EquipmentSlot == slot)
				{
					return type;
				}
			}
			
			return null;
		}
	}
	
	public enum QualityType
	{
		NORMAL("�@��", 80, "��f", 3),
		SPECIAL("�S�O", 60, "��a", 5),
		RARE("�}��", 40, "��e", 7),
		EPIC("�v��", 20, "��d", 9),
		LEGEND("�ǻ�", 10, "��b", 11),
		;
	
		private String Name;
		private String ColorString;
		private int AppearRate;
		private int Amount;
		
		QualityType(String name, int appearrate, String color, int amount)
		{
			this.Name= name;
			this.ColorString = color;
			this.AppearRate = appearrate;
			this.Amount = amount;
		}
			
		public String getName()
		{
			return this.Name;
		}
		
		public String getColorString()
		{
			return this.ColorString;
		}
		
		public int getAppearRate()
		{
			return this.AppearRate;
		}
		
		public int getAmount()
		{
			return this.Amount;
		}
		
		public static QualityType getQualityType(String name)
		{
			for(QualityType quality : QualityType.values())
			{
				if(quality.Name.equals(name))
				{
					return quality;
				}
			}
			
			return null;
		}
	}
}
