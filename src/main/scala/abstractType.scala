/**
  * Created by yuki on 2016/10/12.
  */
class Food
abstract class Animals {
  type SuitableFood <: Food
  def eat(food: SuitableFood)
}

class Grass extends Food
class Cow extends Animals {
  type SuitableFood = Grass
  override def eat(food: Grass) {}
}
