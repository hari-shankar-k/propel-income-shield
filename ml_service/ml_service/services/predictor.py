import joblib
import pandas as pd

risk_model = joblib.load("model/risk_model.pkl")
claim_model = joblib.load("model/claim_model.pkl")

def encode_weather(weather):
    mapping = {"Clear": 0, "Rain": 1, "Thunderstorm": 2}
    return mapping.get(weather, 0)

def calculate_premium(risk_score, past_claims, weather):
    base = 50

    risk_factor = risk_score / 100
    claims_factor = 1 + (past_claims * 0.1)

    if weather in ["Rain", "Thunderstorm"]:
        weather_factor = 1.2
    else:
        weather_factor = 1.0

    premium = base * (1 + risk_factor) * claims_factor * weather_factor
    return float(round(premium, 2))


def predict_all(data, weather, temp):

    weather_encoded = encode_weather(weather)

    bad_weather = 1 if (weather in ["Rain", "Thunderstorm"] or temp > 40 or temp < 5) else 0

    features = pd.DataFrame([{
        "inactivity": data["inactivityHours"],
        "temperature": temp,
        "weather": weather_encoded,
        "past_claims": data["pastClaims"],
        "bad_weather": bad_weather
    }])

    risk_score = risk_model.predict(features)[0]
    claim = claim_model.predict(features)[0]

    premium = calculate_premium(risk_score, data["pastClaims"], weather)

    if risk_score > 70:
        reason = "High inactivity and bad conditions"
    elif risk_score > 50:
        reason = "Moderate risk"
    else:
        reason = "Low risk"

    return {
        "riskScore": int(risk_score),
        "shouldCreateClaim": bool(claim),
        "premiumSuggestion": premium,
        "reason": reason
    }