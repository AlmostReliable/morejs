package testmod.tests;

import com.almostreliable.morejs.BuildConfig;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import testmod.GameTestTemplates;

@GameTestHolder(value = BuildConfig.MOD_ID)
@PrefixGameTestTemplate(false)
public class PotionBrewingTests {

    @GameTest(template = GameTestTemplates.EMPTY)
    public void removeVanillaSplashContainer(GameTestHelper helper) {
        helper.succeedIf(() -> {
            if (helper.getLevel().potionBrewing().isInput(Items.SPLASH_POTION.getDefaultInstance())) {
                throw new GameTestAssertException("Expected that splash potion container was removed!");
            }

            // We still want to check lingering still exist
            if (!helper.getLevel().potionBrewing().isInput(Items.LINGERING_POTION.getDefaultInstance())) {
                throw new GameTestAssertException("Expected that lingering potion container still exists!");
            }
        });
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void addCustomContainer(GameTestHelper helper) {
        helper.succeedIf(() -> {
            PotionBrewing potionBrewing = helper.getLevel().potionBrewing();
            if (!potionBrewing.hasContainerMix(Items.LINGERING_POTION.getDefaultInstance(),
                    Items.APPLE.getDefaultInstance())) {
                throw new GameTestAssertException("Expected that splash potion container was removed!");
            }
        });
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void addPotionBrewing(GameTestHelper helper) {
        helper.succeedIf(() -> {
            PotionBrewing potionBrewing = helper.getLevel().potionBrewing();
            if (!potionBrewing.hasContainerMix(Items.LINGERING_POTION.getDefaultInstance(),
                    Items.APPLE.getDefaultInstance())) {
                throw new GameTestAssertException("Test doesn't match condition");
            }
        });
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void removePotionBrewing(GameTestHelper helper) {
        helper.succeedIf(() -> {
            PotionBrewing potionBrewing = helper.getLevel().potionBrewing();
            ItemStack ingredientItem = Items.GLOWSTONE_DUST.getDefaultInstance();
            ItemStack input = PotionContents.createItemStack(Items.POTION, Potions.HARMING);
            if (potionBrewing.hasPotionMix(input, ingredientItem)) {
                throw new GameTestAssertException("Test doesn't match condition!");
            }
        });
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void addCustomBrewing(GameTestHelper helper) {
        helper.succeedIf(() -> {
            PotionBrewing potionBrewing = helper.getLevel().potionBrewing();
            ItemStack ingredientItem = Items.STICK.getDefaultInstance();
            ItemStack input = Items.OAK_LOG.getDefaultInstance();
            if (!potionBrewing.hasMix(input, ingredientItem)) {
                throw new GameTestAssertException("Test doesn't match condition!");
            }
        });
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void removeCustomBrewing(GameTestHelper helper) {
        helper.succeedIf(() -> {
            PotionBrewing potionBrewing = helper.getLevel().potionBrewing();
            ItemStack ingredientItem = Items.EMERALD.getDefaultInstance();
            ItemStack input = Items.NETHER_STAR.getDefaultInstance();
            if (potionBrewing.hasMix(input, ingredientItem)) {
                throw new GameTestAssertException("Test doesn't match condition!");
            }
        });
    }
}
