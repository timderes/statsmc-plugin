import { useQuery, useQueryClient } from "@tanstack/react-query";
import { createFileRoute, Link } from "@tanstack/react-router";
import { useEffect } from "react";
import { TanStackRouterDevtools } from "@tanstack/react-router-devtools";
import { Card, CardGroup } from "react-bootstrap";
import WeatherIcon from "../components/shared/WeatherIcon";
import getTimeOfDay from "../lib/utils/world/getTimeOfDay";
import { IconSword, IconSwordOff } from "@tabler/icons-react";
import ErrorAlert from "../components/shared/ErrorAlert";
import LoadingSpinner from "../components/shared/LoadingSpinner";

export const Route = createFileRoute("/")({
  component: Dashboard,
  notFoundComponent: () => {
    return <>NOT FOUND!</>;
  },
});

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
    refetchInterval: 25000, // 25 seconds
  });

  useEffect(() => {
    if (isStale) {
      queryClient.invalidateQueries({ queryKey: ["serverInfo"] });
    }
  }, [queryClient, isStale]);

  if (isLoading) return <LoadingSpinner text="Loading Server Information..." />;
  if (isError) return <ErrorAlert message={error.message} />;

  const currentPlayers = serverInfo?.worlds
    .map((world) => world.current_players.length)
    .reduce((a, b) => a + b, 0);

  return (
    <>
      <header>
        <h1 className="fw-bold w-100 d-flex align-items-center justify-content-between">
          Server "{serverInfo?.server_name}"
          <span className="badge text-bg-primary">
            {currentPlayers + "/" + serverInfo?.max_players}
          </span>
        </h1>
        <p className="lead">{serverInfo?.motd}</p>
        <div className="d-flex gap-1">
          <span className="badge text-bg-primary">
            {serverInfo?.server_version}
          </span>
          <span className="badge text-bg-light">{serverInfo?.game_mode}</span>
        </div>
      </header>
      <CardGroup>
        {serverInfo?.worlds.map((world) => (
          <Card body key={world.name}>
            <p className="fs-3 fw-bold">{world.name}</p>
            <p className="d-flex align-items-center gap-1">
              Weather: {WeatherIcon(world.weather)}
            </p>
            <p>Time: {getTimeOfDay(world.time)}</p>
            Current Players:{" "}
            {world.current_players.length === 0 ? (
              <span className="fst-italic">None</span>
            ) : (
              <ul>
                {world.current_players.map((player) => (
                  <li key={player}>
                    <Link
                      to={`/player/$name`}
                      params={{
                        name: player,
                      }}
                    >
                      {player}
                    </Link>
                  </li>
                ))}
              </ul>
            )}
            <hr />
            <div className="hstack justify-content-around">
              <span>Difficulty: {world.difficulty}</span>
              <span className="d-flex align-items-center gap-1">
                PVP {world.pvp ? <IconSword /> : <IconSwordOff />}
              </span>
              <span>Animals {world.allow_animals ? "On" : "Off"}</span>
              <span>Monsters {world.allow_monsters ? "On" : "Off"}</span>
            </div>
          </Card>
        ))}
      </CardGroup>
      <TanStackRouterDevtools position="top-right" />
    </>
  );
}
