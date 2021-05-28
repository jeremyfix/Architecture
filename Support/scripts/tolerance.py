import matplotlib.pyplot as plt
import numpy as np

# We consider a divider with 2 resistors of tolerance p
#  Vin ---
#        |
#        R
#        |- Vout
#        R
#        |
#       ___
#       ///
# With a perfect resistor : Vout = Vin / 2
# but what if the resistor have a tolerance p ?

p = np.linspace(0, 1)
rmin = (1 - p) / (2.0 * (1 + p))
rmax = (1 + p) / (2.0 * (1 - p))

plt.figure()
plt.plot(p,rmin, 'b--')
plt.plot(p, rmax, 'b')
plt.ylabel('ratio Vout/Vin')
plt.xlabel('tolerance p on the resistor')
plt.show()
