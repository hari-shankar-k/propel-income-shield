export default function RiskMonitor() {
  const weatherRisk = "HIGH";
  const estimatedCoverage = 450;

  // Determine color based on risk level
  const getRiskColor = (risk) => {
    switch (risk) {
      case "HIGH":
        return { text: "text-red-400", bg: "bg-red-900", progress: "bg-red-500" };
      case "MEDIUM":
        return { text: "text-yellow-400", bg: "bg-yellow-900", progress: "bg-yellow-500" };
      case "LOW":
        return { text: "text-green-400", bg: "bg-green-900", progress: "bg-green-500" };
      default:
        return { text: "text-gray-400", bg: "bg-gray-900", progress: "bg-gray-500" };
    }
  };

  const colors = getRiskColor(weatherRisk);
  const progressWidth = weatherRisk === "HIGH" ? "w-[80%]" : weatherRisk === "MEDIUM" ? "w-[50%]" : "w-[20%]";

  return (
    <div className="bg-gray-900 p-6 rounded-2xl w-full max-w-sm shadow-xl border border-gray-800">
      <h2 className="text-lg font-semibold mb-4">Live Risk Monitor</h2>

      {/* Weather Risk Progress */}
      <div className="mb-6">
        <p className="text-sm text-gray-400">Weather Risk</p>
        <div className="w-full bg-gray-700 h-2 rounded-full mt-2">
          <div className={`${colors.progress} h-2 ${progressWidth} rounded-full transition-all`}></div>
        </div>
        <p className={`${colors.text} mt-2 font-semibold`}>{weatherRisk}</p>
      </div>

      {/* Weather Indicators */}
      <div className="grid grid-cols-2 gap-3 mb-6">
        <div className="bg-gray-800 p-4 rounded-xl hover:bg-gray-750 transition">
          <p className="text-sm">🌧 Heavy Rain</p>
          <p className="text-xs text-gray-500 mt-1">Trigger: 25mm/hr</p>
        </div>
        <div className="bg-gray-800 p-4 rounded-xl hover:bg-gray-750 transition">
          <p className="text-sm">🌫 Air Quality</p>
          <p className="text-xs text-gray-500 mt-1">Trigger: AQI 400+</p>
        </div>
      </div>

      {/* Auto-Payout Status */}
      <div className="bg-green-900 p-4 rounded-xl border border-green-800">
        <p className="text-green-400 font-semibold">✓ Auto-Payout Ready</p>
        <p className="text-sm text-gray-300 mt-1">
          Estimated Coverage: ₹{estimatedCoverage}
        </p>
      </div>
    </div>
  );
}
