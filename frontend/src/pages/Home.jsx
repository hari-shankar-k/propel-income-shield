import Navbar from "../components/Navbar";
import Hero from "../components/Hero";
import Pricing from "../components/Pricing";

const features = [
  {
    title: "Rain Shield",
    description: "Protect your earnings when heavy rains disrupt your day.",
    icon: "🌧️",
  },
  {
    title: "AQI Shield",
    description: "Stay covered during pollution alerts and unsafe air quality.",
    icon: "🌫️",
  },
  {
    title: "Heat Shield",
    description: "Secure payouts when extreme heat reduces your delivery hours.",
    icon: "🔥",
  },
  {
    title: "Coin Rewards",
    description: "Earn tokens for unused hours and performance bonuses.",
    icon: "🪙",
  },
];

export default function Home() {
  return (
    <div className="bg-black text-white min-h-screen">
      <Navbar />
      <Hero />

      <div className="px-6 lg:px-16 py-16 lg:py-24">
        <div className="max-w-5xl mx-auto">
          <div className="text-center mb-12">
            <p className="text-sm uppercase tracking-[0.3em] text-blue-400">Coverage Made Simple</p>
            <h2 className="text-3xl lg:text-4xl font-bold mt-4">Shield your income from weather, pollution, and delays</h2>
            <p className="text-gray-400 mt-4 max-w-3xl mx-auto">
              Propel Income Shield gives gig partners the confidence to keep working even when conditions turn risky.
            </p>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6 mb-14">
            {features.map((feature) => (
              <div key={feature.title} className="bg-gray-900 border border-gray-800 rounded-3xl p-6 hover:border-blue-500 transition">
                <div className="text-4xl mb-4">{feature.icon}</div>
                <h3 className="font-semibold text-lg mb-2">{feature.title}</h3>
                <p className="text-gray-400 text-sm">{feature.description}</p>
              </div>
            ))}
          </div>

          <div className="bg-gray-900 border border-gray-800 rounded-3xl p-8 grid grid-cols-1 md:grid-cols-3 gap-6">
            <div className="md:col-span-2">
              <h3 className="text-xl font-semibold mb-3">Invite a Friend</h3>
              <p className="text-gray-400 mb-4">Help your friends protect their earnings and earn rewards for every new signup.</p>
              <div className="flex flex-col sm:flex-row gap-3">
                <div className="bg-black/70 rounded-2xl px-4 py-3 flex-1 text-sm text-gray-200 break-all">PROPEL50</div>
                <button
                  onClick={() => navigator.clipboard.writeText("PROPEL50")}
                  className="bg-blue-600 text-white rounded-2xl px-5 py-3 hover:bg-blue-500 transition"
                >
                  Copy Code
                </button>
              </div>
            </div>
            <div className="bg-blue-950 rounded-3xl p-6 flex flex-col justify-center">
              <p className="text-sm uppercase tracking-[0.3em] text-blue-400 mb-4">Referral Bonus</p>
              <p className="text-3xl font-bold">1 month free</p>
              <p className="text-gray-400 mt-3 text-sm">Give your network a smarter shield while earning a reward.</p>
            </div>
          </div>
        </div>
      </div>

      <Pricing />

      <footer className="bg-gray-950 text-gray-400 px-6 lg:px-16 py-8 lg:py-10 border-t border-gray-800 text-center text-sm lg:text-base">
        <p>© 2026 Propel Income Shield. Built for gig workers, powered by AI.</p>
      </footer>
    </div>
  );
}
