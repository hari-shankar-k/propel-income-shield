import joblib
import numpy as np
from config.settings import THRESHOLD

# Load model once
model = joblib.load("model/riskguard_model.pkl")

def predict_risk(data):
    features = [
        data.get("distance", 0),
        data["time_stationary"],
        data["temperature"],
        data["humidity"],
        data["weather"],
        data["hour"],
        data["day"],
        data["is_stationary"],
        data["is_bad_weather"],
        data["is_night"]
    ]

    features = np.array(features).reshape(1, -1)

    prob = model.predict_proba(features)[0][1]
    risk = int(prob > THRESHOLD)

    return {
    "riskScore": float(prob * 100),
    "shouldCreateClaim": bool(prob > THRESHOLD)
}
