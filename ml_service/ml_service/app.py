from flask import Flask, request, jsonify
from services.predictor import predict_all
from services.weather import get_weather
from services.premium import calculate_premium_ai

app = Flask(__name__)

@app.route("/predict", methods=["POST"])
def predict():
    data = request.json

    weather, temp = get_weather(data["latitude"], data["longitude"])

    result = predict_all(data, weather, temp)

    return jsonify(result)


@app.route("/ai/premium", methods=["POST"])
def premium():
    data = request.json
    result = calculate_premium_ai(data)
    return jsonify(result)

if __name__ == "__main__":
    app.run(port=5001, debug=True)