export abstract class ConnectionGraphNode {
  public height : number = 20;
  public width: number = 10;
  public x: number;
  public y: number;
  public textOffset: number = 10;
  public positionOffset : number = 40;
  public padding : number = 55;
  public index : number;

  protected constructor(public id : string, public label: string) {
    this.y = this.positionOffset + this.padding;
  }

  public updatePosition(order : number) {
    this.y = (order * this.positionOffset) + this.padding;
  }

  public getAbsoluteHeight() : number {
    return this.y + this.height;
  }

  public getAbsoluteWidth(frameWidth : number) : number {
    return (frameWidth / 100) * (this.width + this.x);
  }

  public getAbsoluteMidpointX(frameWidth : number) : number {
    return this.getAbsoluteWidth(frameWidth) - ((frameWidth / 100) * this.width);
  }

  public getAbsoluteMidpointY(frameWidth : number) : number {
    return this.getAbsoluteHeight() - (this.height / 2);
  }

  public getAbsoluteX(framewidth: number) : number {
    return (framewidth / 100) * this.x;
  }

}
