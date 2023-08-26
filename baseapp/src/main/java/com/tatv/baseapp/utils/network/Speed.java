package com.tatv.baseapp.utils.network;

/**
 * Created by tatv on 16/11/2022.
 */
public class Speed {
    public int up;
    private int down;

    public Speed(int up, int down) {
        this.up = up;
        this.down = down;
    }

    @Override
    public String toString() {
        return "Speed{" +
                "up=" + up +
                ", down=" + down +
                '}';
    }
}
