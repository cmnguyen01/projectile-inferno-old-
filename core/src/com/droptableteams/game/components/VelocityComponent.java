package com.droptableteams.game.components;

import com.droptableteams.game.LibECS.interfaces.IComponent;

public class VelocityComponent implements IComponent {
    private int _id;
    private String _type;
    private float _baseSpeed;
    private float _dx;
    private float _dy;

    public VelocityComponent(int id, float baseSpeed) {
        _id = id;
        _baseSpeed = baseSpeed;
        _type = "VelocityComponent";
    }

    @Override
    public int getId() {
        return _id;
    }

    @Override
    public String getType() {
        return _type;
    }


    public float getBaseSpeed() {
        return _baseSpeed;
    }

    public void setBaseSpeed(float baseSpeed) {
        _baseSpeed = baseSpeed;
    }

    public float getDx() {
        return _dx;
    }

    public void setDx(float dx) {
        _dx = dx;
    }

    public float getDy() {
        return _dy;
    }

    public void setDy(float dy) {
        _dy = dy;
    }
}