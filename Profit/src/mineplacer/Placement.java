package mineplacer;

import model.enums.MineSubType;

public class Placement {

  private final int horPos;
  private final int verPos;
  private final MineSubType mineSubType;

  public Placement(int horPos, int verPos, MineSubType mineSubType) {
    this.horPos = horPos;
    this.verPos = verPos;
    this.mineSubType = mineSubType;
  }

  public int getHorPos() {
    return horPos;
  }

  public int getVerPos() {
    return verPos;
  }

  public MineSubType getMineSubType() {
    return mineSubType;
  }
}
