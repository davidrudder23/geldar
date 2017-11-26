package org.noses.game.item;

import org.noses.game.GeldarGame;
import org.noses.game.path.Point;

public abstract class BaseStar extends Item {

    public BaseStar(GeldarGame parent, String spriteFilename, Point point) {
        super(parent, spriteFilename, point);
    }

    public void avatarIsNear() {
    }
}
