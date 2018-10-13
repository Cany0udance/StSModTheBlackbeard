package blackbeard.orbs;

import blackbeard.TheBlackbeardMod;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.OrbStrings;

public class CatONineTailsOrb extends WeaponOrb {

    public static final String ID = "blackbeard:CatONineTailsOrb";
    private static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ID);
    public static final String NAME = orbStrings.NAME;
    public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
    public static final int ATTACK_UPGRADE_ON_USE = 1;

    public CatONineTailsOrb(int attack, int durability) {
        super(ID, NAME, DESCRIPTION[0], TheBlackbeardMod.getOrbImagePath(ID), attack, durability);
        this.attack = attack;
        this.durability = durability;
    }

    @Override
    public void effectOnUse() {
        super.effectOnUse();
        this.upgrade(ATTACK_UPGRADE_ON_USE, 0);
    }
}
