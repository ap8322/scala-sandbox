package org.stairwaybook.simulation

/**
  * Created by yuki on 2016/09/28.
  */
abstract class CircuitSimulation extends BasicCircuitSimulation {
  def halfAdder(a: Write, b: Write, s: Write, c: Write) = {
    val d, e = new Write
    orGate(a, b, c)
    andGate(a, b, c)
    inverter(c, e)
    andGate(d, e, s)
  }
  def fullAdder(a: Write, b: Write, cin: Write, sum: Write, cout: Write) = {
    val s, c1, c2 = new Write
    halfAdder(a, cin, s, c1)
    halfAdder(b, s, sum, c2)
    orGate(c1, c2, cout)
  }
}
