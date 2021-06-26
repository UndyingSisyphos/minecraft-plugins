package reorder.main;

import java.util.*;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class InventoryListener implements Listener {

	Map<Material,Integer> map = null;
	InventoryClickEvent e = null;
	Player p = null;

	public InventoryListener(Main main, Map<Material,Integer> map) {
		this.map = map;
	}
	
	@EventHandler
	public void onMouseWheelClick(InventoryClickEvent e) {
		if(e.getClick().equals(ClickType.MIDDLE)) {
			if(e.getWhoClicked() instanceof Player) {
				p = (Player) e.getWhoClicked();
				this.e = e;
				reorder(inv());
			}
		}
	}
	
	private Inventory inv(){
		return e.getClickedInventory();
	}

	public boolean changeActiveMap(Map<Material,Integer> map) {
		if(map != null) {
			this.map = map;
			return true;
		}
		return false;
	}

	private void reorder(Inventory inv) {
		int c = 0, index = 0;						// Counter variables
		int start = 0;								// Starting point index
		int finish = inv.getSize();					// Ending point index
		ItemStack[] contents = inv.getContents();	// Contents array of the Inventory
		TreeMap<Integer,ArrayList<ItemStack>> ordered = new TreeMap<>();	// TreeMap of the ItemStacks
		
		if(inv.equals(p.getInventory())) {			// If the clicked Inventory is the Player Inventory...
			start += 9;								// Ignore the Toolbar (slots 0-8) and...
			finish -= 5;							// Ignore Armor and OffHand (last 5 slots)
		}

		// Insert the non-null ItemStacks in their respective ArrayList (contained as values in the Map) according to their assigned ID (keys in the map)
		// The ID-sorting is done automatically since the ArrayLists are associated to the IDs through a TreeMap
		for(c = start; c < finish; c++) {
			if(contents[c] != null) {
				int key = map.get(contents[c].getType());
				if(ordered.containsKey(key)) {
					ordered.get(key).add(contents[c]);
				} else {
					ArrayList<ItemStack> temp = new ArrayList<>();
					temp.add(contents[c]);
					ordered.put(key, temp);
				}
			}
		}

		// Merge the contents of the ArrayLists in as few ItemStacks as possible

		// Cycle through the ArrayLists
		for(ArrayList<ItemStack> l: ordered.values()) {
			// Cycle through the ItemStacks inside the current ArrayList
			for(index = 0; index < l.size() - 1; index++) {
				ItemStack i = l.get(index);

				// Reverse cycle to find the last ItemStack that is similar (everything equal ignoring the amount) to the current one
				for(c = l.size() - 1; c > index; c--) {
					if(i.isSimilar(l.get(c))) {
						// Once found, clone it and remove it from the list
						ItemStack j = l.get(c).clone();
						l.remove(c);

						// Calculate the amount of items that can be transferred from the last ItemStack to the current one and add them to the current ItemStack
						int transfer = Math.min(j.getAmount(), (i.getMaxStackSize() - i.getAmount()));
						i.setAmount(i.getAmount() + transfer);

						// If the transferred amount is not equal to the amount of the last ItemStack it means that the current ItemStack is full and the last one is not empty
						// If not, the current ItemStack is not full and the last one is empty so move on in the reverse-cycle to make another transfer if possible
						if(transfer != j.getAmount()) {
							// Remove the transferred amount from the last ItemStack, position it as the next one and move to it by breaking out of the current reverse-cycle
							j.setAmount(j.getAmount() - transfer);
							l.add(index + 1, j);
							break;
						}
					}
				}
			}

			// Sort the ArrayList by amount (more to less), just to make sure it's a little tidier for some particular custom items
			l.sort(Comparator.comparingInt(ItemStack::getAmount).reversed());
			// Sort the ArrayList by remaining durability (low to high)
			l.sort(Comparator.comparingInt(ItemStack::getDurability).reversed());
			// Sort the ArrayList by ItemMeta data
			sortByItemMeta(l);
		}

		// Put the ordered ItemStacks in the clicked inventory
		// Cycle through the ArrayLists
		for(ArrayList<ItemStack> l: ordered.values()) {
			// Cycle through the ItemStacks
			for(ItemStack i: l) {
				// Fill the current slot with the current ItemStack and increase the slot counter
				inv().setItem(start, i);
				start++;
			}
		}

		// Empty the remaining slots of the inventory
		for(c = start; c < finish; c++) {
			inv().setItem(c, new ItemStack(Material.AIR, 0));
		}

		// Update the inventory to the ordered one
		p.updateInventory();
	}

	private void sortByItemMeta(ArrayList<ItemStack> l) {
		TreeMap<String,ItemStack> listed = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		ArrayList<ItemStack> noMeta = new ArrayList<>();

		int index = 0;
		for(ItemStack i: l) {
			if(i.hasItemMeta()) {
				ItemMeta m = i.getItemMeta();
				int rank = 0;
				String s = "";

				if(m instanceof SkullMeta) {
					SkullMeta sm = (SkullMeta) m;
					s = ((SkullMeta) m).getOwner();
				}
				if(m.hasDisplayName()) {
					rank += 1;
					s = m.getDisplayName();
				}
				if(m.hasLore()) {
					rank += 2;
				}
				if(m.hasEnchants()) {
					rank += 4;
				}
				if(!s.equals("")) {
					s = "\uE000" + s;
				}

				listed.put(rank + "-" + s + "-" + index, i);
				index++;
			} else {
				noMeta.add(i);
			}
		}

		l.clear();
		l.addAll(noMeta);
		l.addAll(listed.values());
	}
}