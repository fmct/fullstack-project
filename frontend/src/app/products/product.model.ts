export class Product {
  public name: string;
  public price: number;
  public id?: number;

  constructor(name: string, price: number, id: number) {
    this.name = name;
    this.price = price;
    this.id = id;
  }
}
