package com.tatv.baseapp.utils.speed;

public class SpeedUtils {
    /**
     * Bộ lọc tốc độ
     * @param speed double
     * */
    public double filter(double speed){
        double temp = (speed * 3.6);
        if(temp < 4) return 0;
        if(temp < 7) return (float) (speed * 0.7);
        return speed;
    }
    /**
     * Bộ lọc tốc độ
     * @param speed float
     * */
    public float filter(float speed){
        float temp = (float) (speed * 3.6);
        if(temp < 4) return 0;
        if(temp < 7) return (float) (speed * 0.7);
        return speed;
    }

    /**
     * Bộ lọc tốc độ
     * @param speed integer
     * */
    public int filter(int speed){
        int temp = (int) (speed * 3.6);
        if(temp < 4) return 0;
        if(temp < 7) return (int) (speed * 0.7);
        return speed;
    }
}
