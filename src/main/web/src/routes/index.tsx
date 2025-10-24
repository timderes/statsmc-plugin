import { useQuery, useQueryClient } from "@tanstack/react-query";
import { createFileRoute } from "@tanstack/react-router";
import { useEffect } from "react";
import { TanStackRouterDevtools } from "@tanstack/react-router-devtools";

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
            <p>
              Players: {world.current_players.join(", ") || "No players online"}
            </p>
            <p>Environment: {world.environment}</p>
            <p>Difficulty: {world.difficulty}</p>
            <p>PvP: {world.pvp ? "Enabled" : "Disabled"}</p>
          </div>
        ))}
      </div>
      <TanStackRouterDevtools position="top-right" />
    </div>
  );
}
