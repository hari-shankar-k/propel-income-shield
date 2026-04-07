import random

def calculate_premium_ai(data):

    is_new = data.get("isNewUser", False)

    if is_new:
        past_claims = 1
        avg_inactivity = 1.5
        risk_score = 50
    else:
        past_claims = data["pastClaims"]
        avg_inactivity = data["avgInactivityHours"]
        risk_score = data["riskScore"]

    rain_factor = random.uniform(10, 25)

    score = (
        risk_score * 0.5 +
        avg_inactivity * 15 +
        past_claims * 10 +
        rain_factor
    )

    base = 400
    suggested = base + score * 3

    min_premium = suggested * 0.8
    max_premium = suggested * 1.3

    if score < 50:
        risk_level = "LOW"
    elif score < 90:
        risk_level = "MEDIUM"
    else:
        risk_level = "HIGH"

    if risk_level == "LOW":
        plan = "PLAN_A"
    elif risk_level == "MEDIUM":
        plan = "PLAN_B"
    else:
        plan = "PLAN_C"

    if is_new:
        reason = "Estimated using default risk profile"
    elif risk_level == "HIGH":
        reason = "High inactivity and past claims increase risk"
    elif risk_level == "MEDIUM":
        reason = "Moderate working risk detected"
    else:
        reason = "Low risk profile"

    return {
        "suggestedPremium": float(round(suggested, 2)),
        "minPremium": float(round(min_premium, 2)),
        "maxPremium": float(round(max_premium, 2)),
        "riskLevel": risk_level,
        "recommendedPlan": plan,
        "reason": reason
    }