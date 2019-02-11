package com.droptableteams.game.entities.types;

import com.droptableteams.game.util.FirePatternData;
import com.droptableteams.game.util.constants.Directions;

public class EnemyTypeFactory {
    public static EnemyType make(String type) {
        if("EnemyA" == type) {
            return makeEnemyA();
        }
        else if("EnemyB" == type) {
            return makeEnemyB();
        }
        return makeEnemyA();
    }

    public static EnemyType makeEnemyA() {
        FirePatternData fpd = new FirePatternData(Directions.DOWN, 2, (float)(Math.PI), (float)(Math.PI/2), 0.4f, "EnemyBulletA");
        return new EnemyType("EnemyEntity", "EnemyA",fpd, 52, 52, 32);
    }

    public static EnemyType makeEnemyB() {
        FirePatternData fpd = new FirePatternData(Directions.DOWN, 2, (float)(Math.PI), (float)(Math.PI/2), 0.4f, "EnemyBulletA");
        return new EnemyType("EnemyEntity", "EnemyB",fpd, 52, 52, 32);
    }
}
