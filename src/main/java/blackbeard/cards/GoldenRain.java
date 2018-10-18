package blackbeard.cards;

import basemod.abstracts.CustomCard;
import blackbeard.TheBlackbeardMod;
import blackbeard.interfaces.IGoldenCard;
import blackbeard.patches.AbstractCardEnum;
import blackbeard.utils.GoldenCardsUtil;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class GoldenRain extends CustomCard implements IGoldenCard {
    public static final String ID = "blackbeard:GoldenRain";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final int COST = 2;
    private static final int PERCENT_OF_GOLD_GAINED = 3;
    private static final int UPGRADE_PLUS_PERCENT_OF_GOLD_GAINED = 1;

    public GoldenRain() {
        super(ID, NAME, TheBlackbeardMod.getCardImagePath(ID), COST, DESCRIPTION, CardType.ATTACK,
                AbstractCardEnum.BLACKBEARD_BLACK, CardRarity.RARE, CardTarget.ALL_ENEMY);

        this.baseMagicNumber = this.magicNumber = PERCENT_OF_GOLD_GAINED;
        this.isMultiDamage = true;

        setGoldenValuesAndInitializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
    }

    public void setGoldenValuesAndInitializeDescription() {
        this.baseDamage = this.damage = this.magicNumber * CardCrawlGame.goldGained / 100;
        this.rawDescription = GoldenCardsUtil.getGoldenCardDescription(this.upgraded, DESCRIPTION, DESCRIPTION, EXTENDED_DESCRIPTION);
        this.initializeDescription();
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_PERCENT_OF_GOLD_GAINED);
            setGoldenValuesAndInitializeDescription();
        }
    }
}
