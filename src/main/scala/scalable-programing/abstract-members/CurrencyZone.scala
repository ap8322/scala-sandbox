/**
  * Created by yuki on 2016/10/06.
  */
abstract class CurrencyZone {
  type Currency <: AbstractCurrency
  def make(x: Long): Currency

  abstract class AbstractCurrency {
    val amount: Long
    def designation: String
    def *(that: Currency): Currency =
      make(this.amount * that.amount)
    def *(x: Double): Currency =
      make((this.amount * x).toLong)
    def -(that: Currency): Currency =
      make(this.amount - that.amount)
    def /(that: Double) =
      make((this.amount / that).toLong)
    def /(that: Currency) =
      this.amount.toDouble / that.amount
    def from(other: CurrencyZone#AbstractCurrency): Currency =
      make(
        math.round(
          other.amount.toDouble * Converter.exchangeRate(other.designation)(
            this.designation)
        ))
  }
}
