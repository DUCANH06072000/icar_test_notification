package com.tatv.baseapp.utils.tpms;

/**
 * Created by tatv on 15/11/2022.
 */
public class Tire {
    /**
     * Cập nhật thông số lốp
     * @param id :vị trí lốp
     * @param press :giá trị áp suất
     * @param temp :giá trị nhiệt độ
     * @param status :cảnh báo (0 là bình thường, 1 là cảnh cáo áp suất thâos, 2 là cảnh báo áp suất cao, 4 là cảnh báo pin yếu, 8 là cảnh báo nhiệt độ cao)
     * **/
    int id = 0;
    float press = 0;
    int temp = 0;
    int status = 0;

    public Tire() {}

    public Tire(int id, float press, int temp) {
        this.id = id;
        this.press = press;
        this.temp = temp;
    }

    public Tire(int id, float press, int temp, int status) {
        this.id = id;
        this.press = press;
        this.temp = temp;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public Tire setId(int id) {
        this.id = id;
        return this;
    }

    public float getPress() {
        return press;
    }

    public Tire setPress(float press) {
        this.press = press;
        return this;
    }

    public int getTemp() {
        return temp;
    }

    public Tire setTemp(int temp) {
        this.temp = temp;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public Tire setStatus(int status) {
        this.status = status;
        return this;
    }

    public boolean isBatteryWarring(){
        return status == 4;
    }

    @Override
    public String toString() {
        return "Tire{" +
                "id=" + id +
                ", press=" + press +
                ", temp=" + temp +
                ", status=" + status +
                '}';
    }
}
