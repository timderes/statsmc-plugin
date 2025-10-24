import { useQuery, useQueryClient } from "@tanstack/react-query";
import { createFileRoute } from "@tanstack/react-router";
import { useEffect } from "react";
import { TanStackRouterDevtools } from "@tanstack/react-router-devtools";
import { IconCloudRain, IconCloudStorm, IconSun } from "@tabler/icons-react";

export const Route = createFileRoute("/")({
  component: Dashboard,
  notFoundComponent: () => {
    return <>NOT FOUND!</>;
  },
});

const getTimeOfDay = (time: World["time"]) => {
  switch (true) {
    case time > 23000:
      return "Sunrise";
    case time > 18000:
      return "Midnight";
    case time > 13000:
      return "Night";
    case time > 12000:
      return "Sunset";
    case time > 6000:
      return "Noon";
    case time > 2000:
      return "Day";
    default:
      return "Morning";
  }
};

const getWeatherIcon = (currentWeather: World["weather"]) => {
  switch (currentWeather) {
    case "clear":
      return <IconSun />;
    case "rain":
      return <IconCloudRain />;
    case "thunder":
      return <IconCloudStorm />;
  }
};

function Dashboard() {
  const queryClient = useQueryClient();

  const getServerInfo = async () => {
    const URL = "http://localhost:33333/api/";

    const response = await fetch(URL);
    if (!response.ok) {
      throw new Error("Network response was not ok");
    }
    return response.json();
  };

  const {
    data: serverInfo,
    isLoading,
    isError,
    isStale,
    error,
  } = useQuery<ServerInfo>({
    queryKey: ["serverInfo"],
    queryFn: getServerInfo,
  });

  useEffect(() => {
    if (isStale) {
      queryClient.invalidateQueries({ queryKey: ["serverInfo"] });
    }
  }, []);

  if (isLoading) return <div>Loading...</div>;
  if (isError) return <div>Error: {error.message}</div>;

  return (
    <div className="App">
      <h1>StatsMC Dashboard</h1>
      <div style={{ display: "flex", gap: 16 }}>
        {serverInfo?.worlds.map((world) => (
          <div key={world.name}>
            <h2>{world.name}</h2>
            <p>Weather: {getWeatherIcon(world.weather)}</p>
            <p>
              Time: {getTimeOfDay(world.time)} ({world.time})
            </p>
            <ul>
              {world.current_players.map((player) => (
                <li key={player}>{player}</li>
              ))}
            </ul>
          </div>
        ))}
      </div>
      <TanStackRouterDevtools position="top-right" />
    </div>
  );
}
