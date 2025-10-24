import { useQuery, useQueryClient } from "@tanstack/react-query";
import { createFileRoute, Link } from "@tanstack/react-router";
import { useEffect } from "react";
import { TanStackRouterDevtools } from "@tanstack/react-router-devtools";
import { IconCloudRain, IconCloudStorm, IconSun } from "@tabler/icons-react";
import { Card, CardGroup } from "react-bootstrap";

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

  const currentPlayers = serverInfo?.worlds
    .map((world) => world.current_players.length)
    .reduce((a, b) => a + b, 0);

  return (
    <>
      <h1 className="fw-bold w-100 d-flex align-items-center justify-content-between">
        Server "{serverInfo?.server_name}"{" "}
        <span className="badge text-bg-primary">
          {currentPlayers + "/" + serverInfo?.max_players}{" "}
          <small className="fs-6 fw-normal text-uppercase">Players</small>
        </span>
      </h1>
      <p className="lead">{serverInfo?.motd}</p>
      <CardGroup>
        {serverInfo?.worlds.map((world) => (
          <Card body key={world.name}>
            <p className="fs-3 fw-bold">{world.name}</p>
            <p className="d-flex align-items-center gap-1">
              Weather: {getWeatherIcon(world.weather)}
            </p>
            <p>
              Time: {getTimeOfDay(world.time)} ({world.time})
            </p>
            <ul>
              {world.current_players.map((player) => (
                <li key={player}>
                  <Link to={`/player/${player}`}>{player}</Link>
                </li>
              ))}
            </ul>
          </Card>
        ))}
      </CardGroup>
      <TanStackRouterDevtools position="top-right" />
    </>
  );
}
