import { useQuery, useQueryClient } from "@tanstack/react-query";
import { createFileRoute, Link } from "@tanstack/react-router";
import { useEffect } from "react";
import { Alert, Table } from "react-bootstrap";

export const Route = createFileRoute("/players")({
  component: RouteComponent,
});

function RouteComponent() {
  const queryClient = useQueryClient();

  const getAllPlayers = async () => {
    const URL = `http://localhost:33333/api/players`;

    const response = await fetch(URL);
    if (!response.ok) {
      throw new Error("Network response was not ok");
    }
    return response.json();
  };

  const { data, isLoading, isError, isStale, error } = useQuery<{
    players: {
      name: string;
      is_online: boolean;
      current_world: string | null;
      last_seen: number;
      first_joined: number;
    }[];
    count: number;
  }>({
    queryKey: ["players"],
    queryFn: getAllPlayers,
    refetchInterval: 60000, // Refetch every 60 seconds
  });

  useEffect(() => {
    if (isStale) {
      queryClient.invalidateQueries({ queryKey: ["players"] });
    }
  }, [queryClient, isStale]);

  return (
    <>
      <header className="vstack gap-3">
        <h1 className="fw-bold">Players</h1>
      </header>

      {isLoading && <p>Loading players...</p>}
      {isError && <Alert variant="danger">Error: {error.message}</Alert>}

      <Table>
        <thead>
          <tr>
            <th>Players</th>
            <th>Status</th>
            <th>Current World</th>
            <th>Last Seen</th>
            <th>First Joined</th>
          </tr>
        </thead>
        <tbody>
          {(() => {
            const players = data?.players ?? [];

            if (players.length === 0) {
              return (
                <tr>
                  <td colSpan={5}>No players available</td>
                </tr>
              );
            }

            return players
              .sort((a, b) => b.last_seen - a.last_seen)
              .map((player) => (
                <tr key={player.name}>
                  <td>
                    {player.name ? (
                      <Link
                        to={`/player/$name`}
                        params={{
                          name: player.name,
                        }}
                      >
                        {player.name}
                      </Link>
                    ) : (
                      <span className=" fst-italic">Unknown Player</span>
                    )}
                  </td>
                  <td>{player.is_online ? "Online" : "Offline"}</td>
                  <td>{player.current_world ?? "-"}</td>
                  <td>
                    {new Date(
                      player?.last_seen ?? Date.now()
                    ).toLocaleString() ?? "-"}
                  </td>
                  <td>
                    {player?.first_joined
                      ? new Date(player?.first_joined).toLocaleString()
                      : "?"}
                  </td>
                </tr>
              ));
          })()}
        </tbody>
      </Table>
    </>
  );
}
