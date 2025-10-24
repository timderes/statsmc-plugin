import { useQuery, useQueryClient } from "@tanstack/react-query";
import { createFileRoute, useParams } from "@tanstack/react-router";
import { useEffect } from "react";
import { Alert } from "react-bootstrap";

export const Route = createFileRoute("/player/$name")({
  component: RouteComponent,
});

function RouteComponent() {
  const params = useParams({ from: "/player/$name" });
  const name = params.name;

  const queryClient = useQueryClient();

  const getPlayerInfo = async () => {
    if (!name) {
      throw new Error("No player name provided");
    }
    const URL = `http://localhost:33333/api/player?name=${name}`;

    const response = await fetch(URL);
    if (!response.ok) {
      throw new Error("Network response was not ok");
    }
    return response.json();
  };

  const {
    data: playerStats,
    isLoading,
    isError,
    isStale,
    error,
  } = useQuery<PlayerStats>({
    queryKey: ["playerStats", name],
    queryFn: getPlayerInfo,
  });

  useEffect(() => {
    if (isStale) {
      queryClient.invalidateQueries({ queryKey: ["playerStats", name] });
    }
  }, []);

  return (
    <>
      <h1 className="fw-bold">{name}</h1>
      {isLoading && <p>Loading player stats...</p>}
      {isError && <Alert variant="danger">Error: {error.message}</Alert>}

      <pre>{JSON.stringify(playerStats, null, 2)}</pre>
    </>
  );
}
