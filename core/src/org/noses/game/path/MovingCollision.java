package org.noses.game.path;

import org.noses.game.character.Avatar;
import org.noses.game.character.Dragon;
import org.noses.game.character.MovingCharacter;

import java.util.List;
import java.util.Optional;

public class MovingCollision {

    private static MovingCollision instance;

    private List<Dragon> dragons;

    private Avatar avatar;

    private MovingCollision() {
    }

    public static MovingCollision getInstance() {
        if (instance == null) {
            instance = new MovingCollision();
        }

        return instance;
    }

    public void handleCollision() {
        Optional<Dragon> dragon = dragons.stream()
                .filter(d -> d.getX() == avatar.getX() && d.getY() == avatar.getY())
                .findFirst();

        if (dragon.isPresent()) {
            //System.out.println("handling collision of " + dragon + " (" + dragon.get().getX() + "," + dragon.get().getY() + ") with " + avatar + " (" + avatar.getX() + "," + avatar.getY() + ")");
            avatar.collideWith(dragon.get());
        }

    }

    public List<Dragon> getDragons() {
        return dragons;
    }

    public void setDragons(List<Dragon> dragons) {
        this.dragons = dragons;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }
}
