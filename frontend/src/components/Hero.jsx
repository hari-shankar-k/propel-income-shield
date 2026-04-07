import { useRef } from "react";
import RiskMonitor from "./RiskMonitor";

export default function Hero() {
  const pricingRef = useRef(null);
  const howItWorksRef = useRef(null);

  const scrollToSection = (ref) => {
    ref.current?.scrollIntoView({ behavior: "smooth" });
  };

  return (
    <div className="bg-black text-white">
      {/* Hero Section */}
      <div className="flex flex-col lg:flex-row justify-between items-center lg:items-start px-6 lg:px-16 py-12 lg:py-20 gap-8 lg:gap-12 min-h-screen">
        {/* LEFT TEXT */}
        <div className="max-w-xl flex-1 w-full">
          <h1 className="text-4xl lg:text-5xl font-bold leading-tight">
            Don't Let Rain <br />
            <span className="text-blue-500">Stop Your Earnings.</span>
          </h1>

          <p className="mt-6 text-gray-400 text-base lg:text-lg">
            Automated income protection for delivery partners. Get paid instantly
            when extreme weather disruptions occur.
          </p>

          <div className="mt-8 flex flex-col sm:flex-row gap-4 space-x-0 sm:space-x-4">
            <button
              onClick={() => scrollToSection(pricingRef)}
              className="bg-blue-600 px-6 py-3 rounded-xl font-semibold hover:bg-blue-700 transition w-full sm:w-auto"
            >
              Protect My Income
            </button>

            <button
              onClick={() => scrollToSection(howItWorksRef)}
              className="border border-gray-600 px-6 py-3 rounded-xl hover:border-blue-500 hover:text-blue-500 transition w-full sm:w-auto"
            >
              How AI Works
            </button>
          </div>

          <div className="mt-6 flex flex-col sm:flex-row gap-3 sm:gap-6 text-sm text-gray-400">
            <span>✔ No Paperwork</span>
            <span>✔ Weekly Payouts</span>
            <span>✔ AI Fraud Detection</span>
          </div>
        </div>

        {/* RIGHT CARD - Risk Monitor */}
        <div className="w-full sm:w-auto flex-1 flex justify-center lg:justify-end">
          <RiskMonitor />
        </div>
      </div>

      {/* How AI Works Section */}
      <div
        ref={howItWorksRef}
        id="how-it-works"
        className="px-6 lg:px-16 py-16 lg:py-20 border-t border-gray-800 bg-gray-950"
      >
        <h2 className="text-3xl lg:text-4xl font-bold mb-12 text-center">How AI Works</h2>
        
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 lg:gap-8 max-w-5xl mx-auto">
          {[
            {
              title: "Smart Risk Detection",
              description:
                "Our AI monitors real-time weather patterns and environmental factors to detect risks in your area.",
              icon: "🔍",
            },
            {
              title: "Auto-Payouts",
              description:
                "When a trigger event occurs, coverage automatically activates. No claims to file, instant payouts.",
              icon: "⚡",
            },
            {
              title: "Fraud Prevention",
              description:
                "Advanced ML models verify authenticity, ensuring fair payouts and protecting the ecosystem.",
              icon: "🛡️",
            },
          ].map((item, idx) => (
            <div key={idx} className="bg-gray-900 p-6 rounded-xl border border-gray-800 hover:border-blue-500 transition">
              <p className="text-4xl mb-4">{item.icon}</p>
              <h3 className="text-lg lg:text-xl font-semibold mb-2">{item.title}</h3>
              <p className="text-gray-400 text-sm lg:text-base">{item.description}</p>
            </div>
          ))}
        </div>
      </div>

      {/* Pricing Section Ref */}
      <div ref={pricingRef} id="pricing" className="scroll-mt-20" />
    </div>
  );
}
