package com.droptableteams.game.entities;

import com.droptableteams.game.LibECS.interfaces.IEntity;

public class PlayerBulletEntity implements IEntity {
    private int _id;
    private String _type;

    public PlayerBulletEntity(int id) {
        _id = id;
        _type = "PlayerBulletEntity";
    }

    @Override
    public int getId() {
        return _id;
    }

    @Override
    public String getType() {
        return _type;
    }
}