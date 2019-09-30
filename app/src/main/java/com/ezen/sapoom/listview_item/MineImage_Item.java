package com.ezen.sapoom.listview_item;

public class MineImage_Item {
    private String ID1;
    private String ID2;
    private String ID3;

    public MineImage_Item(String ID1, String ID2, String ID3) {
        this.ID1 = ID1;
        this.ID2 = ID2;
        this.ID3 = ID3;
    }

    public String getID1() {
        return ID1;
    }

    public void setID1(String ID1) {
        this.ID1 = ID1;
    }

    public String getID2() {
        return ID2;
    }

    public void setID2(String ID2) {
        this.ID2 = ID2;
    }

    public String getID3() {
        return ID3;
    }

    public void setID3(String ID3) {
        this.ID3 = ID3;
    }

    // 아이탬을 3개로 늘림
}
