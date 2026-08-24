package com.hronon.limitlesscrafting.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hronon.limitlesscrafting.utils.ItemHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.hronon.limitlesscrafting.LimitlessCraft.MODID;

public class WorkbenchRecipe implements Recipe<SimpleContainer>
{
    private final NonNullList<ItemStack> input;
    private final ItemStack output;
    private final ResourceLocation recipe_id;

    public WorkbenchRecipe(NonNullList<ItemStack> input, ItemStack output, ResourceLocation recipe_id)
    {
        this.input = input;
        this.output = output;
        this.recipe_id = recipe_id;
    }

    public @NotNull NonNullList<ItemStack> inputs()
    {
        return this.input;
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(SimpleContainer p_44001_, RegistryAccess p_267165_) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess p_267052_) {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return recipe_id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<WorkbenchRecipe>
    {
        public static final Type INSTANCE = new Type();
        public static final String ID = "workbench_limitless";
    }

    public static class Serializer implements RecipeSerializer<WorkbenchRecipe>
    {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(MODID, "workbench_limitless");

        @Override
        public WorkbenchRecipe fromJson(ResourceLocation recipe_id, JsonObject serialized_recipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(serialized_recipe, "output"));

            JsonArray ingredients = GsonHelper.getAsJsonArray(serialized_recipe, "ingredients");
            NonNullList<ItemStack> input = NonNullList.create();

            for (var element : ingredients)
            {
//                JsonObject ingredient = element.getAsJsonObject();
//                var item_id = ingredient.get("item").getAsString();
//                var count = ingredient.get("count").getAsInt();

//                var item = ItemHelper.from_string(item_id);
//                item.setCount(count);
                var item = ShapedRecipe.itemStackFromJson(element.getAsJsonObject());
                input.add(item);
            }

            return new WorkbenchRecipe(input, output, recipe_id);
        }

        @Override
        public @Nullable WorkbenchRecipe fromNetwork(ResourceLocation recipe_id, FriendlyByteBuf buffer) {
            NonNullList<ItemStack> input = NonNullList.withSize(buffer.readInt(), ItemStack.EMPTY);

            for (int i = 0; i < input.size(); i++)
            {
                input.add(buffer.readItem());
            }

            ItemStack output = buffer.readItem();

            return new WorkbenchRecipe(input, output, recipe_id);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, WorkbenchRecipe recipe) {
            buffer.writeInt(recipe.input.size());

            for (ItemStack item : recipe.input)
            {
                buffer.writeItemStack(item, false);
            }

            buffer.writeItemStack(recipe.getResultItem(null), false);
        }
    }


}
