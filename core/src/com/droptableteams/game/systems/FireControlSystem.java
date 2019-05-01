package com.droptableteams.game.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.droptableteams.game.LibECS.ComponentManager;
import com.droptableteams.game.LibECS.ECSEngine;
import com.droptableteams.game.LibECS.interfaces.ISystem;
import com.droptableteams.game.components.*;
import com.droptableteams.game.components.game.AssetManagerComponent;
import com.droptableteams.game.components.game.GameCheatsComponent;
import com.droptableteams.game.util.constants.Enums;
import com.droptableteams.game.util.constants.SystemUpdateOrder;
import com.droptableteams.game.util.types.BulletType;
import com.droptableteams.game.util.types.BulletTypeFactory;
import com.droptableteams.game.util.data.BulletData;
import com.droptableteams.game.builders.BulletEntityBuilder;
import com.droptableteams.game.util.constants.SpecialEntityIds;
import com.droptableteams.game.util.types.SubtypeManager;

public class FireControlSystem implements ISystem {
    private int _id;
    private String _type;
    private ComponentManager _cm;

    public FireControlSystem(int id) {
        _id = id;
        _type = "FireControlSystem";
        _cm = ComponentManager.getInstance();
    }

    @Override
    public int getId() {
        return _id;
    }

    @Override
    public String getType() {
        return _type;
    }

    @Override
    public void update() {
        LocationComponent lc = (LocationComponent)ComponentManager.getInstance().getComponent(_id, "LocationComponent");
        FireControlComponent fcc = (FireControlComponent)_cm.getComponent(_id, "FireControlComponent");
        FirePatternComponent fpc = (FirePatternComponent)_cm.getComponent(_id, "FirePatternComponent");
        AssetManagerComponent amc = (AssetManagerComponent)_cm.getComponent(SpecialEntityIds.GAME_ENTITY, "AssetManagerComponent");
        GameCheatsComponent gcc = (GameCheatsComponent) _cm.getComponent(SpecialEntityIds.GAME_ENTITY, "GameCheatsComponent");
        LocationComponent plc = (LocationComponent)ComponentManager.getInstance().getComponent(SpecialEntityIds.PLAYER_ENTITY, "LocationComponent");

        // apply fire pattern rotation
        float newDirection = fpc.getBaseDirection();

        switch(fcc.fireDirectionType) {
            case ANGLE:
                newDirection = (fpc.getDeltaTheta()*Gdx.graphics.getDeltaTime()*gcc.getSpeedMultiplier())+fpc.getBaseDirection();
                break;
            case OUTWARD:
                float xLen = plc.getX()- lc.getX();
                float yLen = plc.getY() - lc.getY();
                newDirection = atan(xLen, yLen);
                break;
            case ATPLAYER:
                // handled by TargetPlayerSystem
        }

        fpc.setBaseDirection(newDirection);

        if(fcc.isFiring()) {
            long time = System.nanoTime();
            long deltaTime = time - fcc.getLastFired();
            if((float)(deltaTime/Math.pow(10,9)) > fpc.rateOfFire) {
                spawnBullets(fpc, amc, lc.getX(), lc.getY());
                fcc.setLastFired(time);
            }
        }
    }

    private void spawnBullets(FirePatternComponent fpc, AssetManagerComponent amc, float x, float y) {
        BulletEntityBuilder builder = BulletEntityBuilder.getInstance(amc.getAssetManager());
        ECSEngine engine = ECSEngine.getInstance(SystemUpdateOrder.get());
        BulletType bt = (BulletType)SubtypeManager.getInstance().getSubtype(fpc.getBulletType());
        int numBullets = fpc.getNumberOfBullets();
        float baseDirection = fpc.getBaseDirection();
        float angle = fpc.getDividingAngle();

        float offset = 0;
        if(Math.PI != angle && numBullets > 1) {
            offset = -1*(angle * (numBullets-1))/2;
        }
        for(int i = 0; i < numBullets; i++) {
            float direction = baseDirection + offset;
            BulletData bd = new BulletData(direction, 0, x, y, _id, bt.subtype);
            builder.setBuildData(bd);
            engine.addEntity(builder);
            offset += angle;
        }
    }

    private float atan(float x, float y) {
        return -1f*(float)(Math.atan2(x, y) - Math.PI/2);
    }
}
