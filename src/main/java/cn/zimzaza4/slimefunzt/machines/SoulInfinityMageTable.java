package cn.zimzaza4.slimefunzt.machines;

import cn.zimzaza4.slimefunzt.SlimefunZT;
import cn.zimzaza4.slimefunzt.lists.Items;
import io.github.mooy1.infinitylib.items.StackUtils;
import io.github.mooy1.infinitylib.recipes.RecipeMap;
import io.github.mooy1.infinitylib.recipes.RecipeOutput;
import io.github.mooy1.infinitylib.recipes.ShapedRecipe;
import io.github.mooy1.infinitylib.slimefun.AbstractTickingContainer;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import lombok.NonNull;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import me.mrCookieSlime.Slimefun.cscorelib2.protection.ProtectableAction;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class SoulInfinityMageTable extends AbstractTickingContainer {
    public final static int[] BOARD = new int []{
        0, 1, 2, 3, 4, 5, 6, 7, 8,
        9,         13     ,16,17,
        18,        22,23  ,25,26,
        27,        31,32  ,34,35
    };
    public final static int[] INPUT = new int []{
            10,11,12,19,20,21,28,29,30
    };
    public static final RecipeMap<SlimefunItemStack> RECIPE_MAP = new RecipeMap<>(ShapedRecipe::new);

     public static final RecipeType Type = new RecipeType(SlimefunZT.getInstance().getKey("soul_infinity_magic_table"), Items.Soul_IE_mage_table,(stacks, stack) -> {
         SlimefunItemStack item = (SlimefunItemStack) stack;
         RECIPE_MAP.put(stacks, item);
     }, "");
    public SoulInfinityMageTable(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
        new BlockMenuPreset(getId(), "&c&l无尽魔法桌") {
            @Override
            public void init() {
                setupMenu(this);
            }

            @Override
            public boolean canOpen(Block block, Player player) {
                if (player.hasPermission("slimefun.inventory.bypass")|| SlimefunPlugin.getProtectionManager().hasPermission(player, block, ProtectableAction.INTERACT_BLOCK)){
                    if (){
                        return true;
                    }
                }else{return false;}
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow itemTransportFlow) {
                return new int[0];
            }
        };
    }

    @Override
    protected void setupMenu(@NotNull BlockMenuPreset preset) {
        for (int i : BOARD) {
            preset.addItem(i, new CustomItem(Material.BLUE_STAINED_GLASS_PANE), ChestMenuUtils.getEmptyClickHandler());
        }
        preset.addItem(14, new CustomItem(Material.ORANGE_STAINED_GLASS_PANE, "输入灵魂合成剂>>>"));
        preset.addItem(24, new CustomItem(Material.GREEN_STAINED_GLASS_PANE, "&c合成"));

    }

    @NotNull
    @Override
    protected int[] getTransportSlots(@NotNull DirtyChestMenu dirtyChestMenu, @NotNull ItemTransportFlow itemTransportFlow, ItemStack itemStack) {
        return new int[0];
    }

    @Override
    protected void tick(@NotNull BlockMenu blockMenu, @NotNull Block block) {
    }

    @Override
    protected void onBreak(@NonNull BlockBreakEvent event, @NonNull BlockMenu inv, @NonNull Location loc){
        inv.dropItems(loc, INPUT);
        inv.dropItems(loc, 33);
    }
    @Override
    public void onNewInstance(@NonNull BlockMenu present, @NonNull Block b){
            present.addMenuClickHandler(24, (p, slot,item, action)->{
                craft(b, present, p);
                return false;
            });
        }

    }
    public void craft(@NonNull Block b, @NonNull BlockMenu inventory, @NonNull Player player){
        ItemStack Energy = inventory.getItemInSlot(14);
        if (Energy==null){
            player.sendMessage("&c缺少灵魂合成剂");
            return;
        }

        ItemStack One_E = Energy.clone();
        One_E.setAmount(1);

        if (One_E.isSimilar(Items.Soul_Amey)){

            Energy.setAmount(Energy.getAmount()-1);

            RecipeOutput<SlimefunItemStack> output = RECIPE_MAP.get(StackUtils.arrayFrom(inventory, INPUT));
            if (output==null){
                return;
            }
            if (inventory.fits(output.getOutput(), 33) ){
                return;
            }
            output.consumeInput();
            player.sendMessage("§a成功合成成");
            inventory.pushItem(output.getOutput().clone(), 33);
        }else{
            player.sendMessage("&c请放入正确的灵魂合成剂!");

        }
    }
}