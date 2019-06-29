package blackbeard.cards;

import basemod.abstracts.CustomCard;
import blackbeard.TheBlackbeardMod;
import blackbeard.enums.CardColorEnum;
import blackbeard.interfaces.IGoldenCard;
import blackbeard.utils.GoldenCardsUtils;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class GoldenDefense extends CustomCard implements IGoldenCard {
    public static final String ID = "blackbeard:GoldenDefense";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final int COST = 1;
    private static final int BLOCK_AMOUNT = 0;
    private static final int UPGRADE_BLOCK_AMOUNT = 3;

    public GoldenDefense() {
        super(ID, NAME, TheBlackbeardMod.getCardImagePath(ID), COST, DESCRIPTION, CardType.SKILL,
                CardColorEnum.BLACKBEARD_BLACK, CardRarity.COMMON, CardTarget.SELF);

        this.baseMagicNumber = this.magicNumber = BLOCK_AMOUNT;

        setGoldenValuesAndUpdateDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
    }

    @Override
    public void applyPowers() {
        setGoldenValuesAndUpdateDescription();
        super.applyPowers();
    }

    @Override
    public void setGoldenValuesAndUpdateDescription() {
        this.baseBlock = this.block = this.magicNumber + (3 * GoldenCardsUtils.getBlackbeardGoldGained() / 200);
        this.rawDescription = GoldenCardsUtils.getGoldenCardDescription(this.upgraded, DESCRIPTION, UPGRADE_DESCRIPTION, EXTENDED_DESCRIPTION);
        this.initializeDescription();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_BLOCK_AMOUNT);
            setGoldenValuesAndUpdateDescription();
        }
    }
}
