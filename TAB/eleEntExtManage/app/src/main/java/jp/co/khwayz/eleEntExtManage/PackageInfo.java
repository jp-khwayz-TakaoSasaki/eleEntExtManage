package jp.co.khwayz.eleEntExtManage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PackageInfo {
    private String name;
    private int breakdown;
    private int quantity;
    public PackageInfo(String name, int breakdown, int quantity) {
       this.name = name;
       this.breakdown = breakdown;
       this.quantity = quantity;
    }
}
