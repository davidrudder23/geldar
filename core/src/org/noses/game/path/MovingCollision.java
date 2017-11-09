package org.noses.game.path;

import org.noses.game.character.Avatar;
import org.noses.game.character.Dragon;
import org.noses.game.character.MovingCharacter;

import java.util.List;
import java.util.Optional;

public class MovingCollision {

    private static MovingCollision instance;

    private List<MovingCharacter> movingCharacters;

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
        Optional<MovingCharacter> movingCharacter = movingCharacters.stream()
                .filter(d -> d.getX() == avatar.getX() && d.getY() == avatar.getY())
                .findFirst();

        if (movingCharacter.isPresent()) {
            //System.out.println("handling collision of " + movingCharacter + " (" + movingCharacter.get().getX() + "," + movingCharacter.get().getY() + ") with " + avatar + " (" + avatar.getX() + "," + avatar.getY() + ")");
            avatar.collideWith(movingCharacter.get());
        }

    }

    public List<MovingCharacter> getMovingCharacters() {
        return movingCharacters;
    }

    public void setMovingCharacters(List<MovingCharacter> movingCharacters) {
        this.movingCharacters = movingCharacters;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }
}
