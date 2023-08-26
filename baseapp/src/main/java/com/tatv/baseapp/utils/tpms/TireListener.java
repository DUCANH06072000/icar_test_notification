package com.tatv.baseapp.utils.tpms;
/**
 * Created by tatv on 15/11/2022.
 */
public interface TireListener{
    /**
     * Khớp lốp thành công
     * @param id : vị trí lốp
     * @param tireId : id lốp
     * */
    void onTireJointSuccess(int id, String tireId);

    /**
     * Lấy id 4 lốp
     * */
    void onTireId(String tireId1, String tireId2, String tireId3, String tireId4);

    /**
     * Đảo lốp thành công
     * */
    void onTireReverseSuccess();


    /**
     * Cập nhật thông số lốp
     * @param tire :thông tin lốp
     * **/
    void onTireValueChanged(Tire tire);
}