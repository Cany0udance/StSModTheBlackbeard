package blackbeard.patches;

import basemod.ReflectionHacks;
import blackbeard.cards.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class LongTitlesPatches {

    private static List<String> cardIDsToFixInEnglish = Arrays.asList(CloakAndCannonball.ID, HumongousCannonball.ID, IntimidatingStrike.ID, TheDrunkenSailor.ID, WeaponProficiency.ID);
    private static List<String> cardIDsToFixInPolish = Arrays.asList(IntimidatingStrike.ID, RearmingStrike.ID, WeaponMastery.ID);
    private static List<String> cardIDsToFixInRussian = Arrays.asList(BountyHunter.ID, FinalBarrage.ID, IntimidatingStrike.ID, Lifeboat.ID, MegaUpgrade.ID, RearmingStrike.ID, TacticalRetreat.ID, WeaponMastery.ID);

    @SpirePatch(clz = FontHelper.class, method = "initialize", paramtypez = {})
    public static class FontHelperPatch {

        public static final Logger logger = LogManager.getLogger(FontHelperPatch.class);

        public static BitmapFont smallerCardTitleFontSmallN;
        public static BitmapFont smallerCardTitleFontSmallL;

        @SpireInsertPatch(locator = Locator.class)
        public static void CardTitleFontInsert() throws Exception {
            Method prepFontMethod = FontHelper.class.getDeclaredMethod("prepFont", float.class, boolean.class);
            prepFontMethod.setAccessible(true);
            smallerCardTitleFontSmallN = (BitmapFont) prepFontMethod.invoke(null, 17.0f, true);
            smallerCardTitleFontSmallL = (BitmapFont) prepFontMethod.invoke(null, 17.0f, true);
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior method) throws Exception {
                Matcher matcher = new Matcher.FieldAccessMatcher(FontHelper.class, "cardTitleFont_small_N");
                return LineFinder.findInOrder(method, matcher);
            }
        }

    }

    @SpirePatch(clz = FontHelper.class, method = "initialize", paramtypez = {})
    public static class FontHelperSingleCardViewPatch {

        public static final Logger logger = LogManager.getLogger(FontHelperSingleCardViewPatch.class);

        public static BitmapFont smallerSCPCardTitleFontSmall;

        @SpireInsertPatch(locator = Locator.class)
        public static void SCPCardTitleFontLocatorInsert() throws Exception {
            Method prepFontMethod = FontHelper.class.getDeclaredMethod("prepFont", float.class, boolean.class);
            prepFontMethod.setAccessible(true);
            smallerSCPCardTitleFontSmall = (BitmapFont) prepFontMethod.invoke(null, 34.0f, true);
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior method) throws Exception {
                Matcher matcher = new Matcher.FieldAccessMatcher(FontHelper.class, "SCP_cardTitleFont_small");
                return LineFinder.findInOrder(method, matcher);
            }
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "renderTitle", paramtypez = {SpriteBatch.class})
    public static class LongTitlesFixPatch {

        private static BitmapFont originalCardTitleFontSmallN;
        private static BitmapFont originalCardTitleFontSmallL;

        public static void Prefix(AbstractCard card, SpriteBatch sb) {
            originalCardTitleFontSmallN = FontHelper.cardTitleFont_small_N;
            originalCardTitleFontSmallL = FontHelper.cardTitleFont_small_L;

            if (shouldFixLongTitle(card)) {
                FontHelper.cardTitleFont_small_N = FontHelperPatch.smallerCardTitleFontSmallN;
                FontHelper.cardTitleFont_small_L = FontHelperPatch.smallerCardTitleFontSmallL;
            }
        }

        public static void Postfix(AbstractCard card, SpriteBatch sb) {
            FontHelper.cardTitleFont_small_N = originalCardTitleFontSmallN;
            FontHelper.cardTitleFont_small_L = originalCardTitleFontSmallL;
        }

    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderTitle", paramtypez = {SpriteBatch.class})
    public static class LongTitlesSingleCardViewFixPatch {

        private static BitmapFont originalSCPCardTitleFontSmall;

        public static void Prefix(SingleCardViewPopup singleCardViewPopup, SpriteBatch sb) {
            originalSCPCardTitleFontSmall = FontHelper.SCP_cardTitleFont_small;

            AbstractCard card = (AbstractCard) ReflectionHacks.getPrivate(singleCardViewPopup, SingleCardViewPopup.class, "card");
            if (shouldFixLongTitle(card)) {
                FontHelper.SCP_cardTitleFont_small = FontHelperSingleCardViewPatch.smallerSCPCardTitleFontSmall;
            }
        }

        public static void Postfix(SingleCardViewPopup singleCardViewPopup, SpriteBatch sb) {
            FontHelper.SCP_cardTitleFont_small = originalSCPCardTitleFontSmall;
        }

    }

    private static boolean shouldFixLongTitle(AbstractCard card) {
        if (Settings.language == Settings.GameLanguage.ENG) {
            if (cardIDsToFixInEnglish.contains(card.cardID)) {
                return true;
            }
        }
        if (Settings.language == Settings.GameLanguage.POL) {
            if (cardIDsToFixInPolish.contains(card.cardID)) {
                return true;
            }
        }
        if (Settings.language == Settings.GameLanguage.RUS) {
            if (cardIDsToFixInRussian.contains(card.cardID)) {
                return true;
            }
        }
        return false;
    }

}
