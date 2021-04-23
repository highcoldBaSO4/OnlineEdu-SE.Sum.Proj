import numpy as np
from tensorflow import keras
import types



mp = "F:\\python\\OnlineStudy\\src\\hardworking_10^6_students.h5"
model = keras.models.load_model(mp)

pre = [200, 100, 220, 120, 220, 190, 240, 180, 220, 172, 260, 100]
pre = np.reshape(pre, (6, 2))
print(model.predict(pre))

