import { useQuery, useQueryClient } from "@tanstack/react-query";
import { createFileRoute, useParams } from "@tanstack/react-router";
import { useEffect } from "react";
import { Badge, Stack, Table } from "react-bootstrap";
import ErrorAlert from "../../components/shared/ErrorAlert";
import LoadingSpinner from "../../components/shared/LoadingSpinner";
import { getTotalDistance } from "../../lib/utils/stats/getTotalDistance";
import { getPlaytime } from "../../lib/utils/stats/getPlaytime";

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
    refetchInterval: 60000, // Refetch every 60 seconds
  });

  useEffect(() => {
    if (isStale) {
      queryClient.invalidateQueries({ queryKey: ["playerStats", name] });
    }
  }, [queryClient, isStale, name]);

  return (
    <>
      <header className="vstack gap-3">
        <h1 className="fw-bold">
          {name}{" "}
          <small>
            <Badge bg={playerStats?.player.is_online ? "primary" : "secondary"}>
              {playerStats?.player.is_online ? "Online" : "Offline"}
            </Badge>
          </small>
        </h1>

        <p className="fst-italic">
          {playerStats?.player.current_world
            ? `Currently playing in "${playerStats.player.current_world}"`
            : ""}
        </p>
        <hr />
        {playerStats?.player ? (
          <>
            <Stack
              direction="horizontal"
              className="text-center align-items-start"
              gap={5}
            >
              <div>
                <strong className="fs-5 fw-bold">
                  {getTotalDistance(playerStats?.statistics ?? {}, "METRIC")}
                </strong>
                <p className=" text-text-uppercase text-opacity-75">
                  Distance Traveled
                </p>
              </div>

              <div>
                <strong className="fs-5 fw-bold">
                  {getPlaytime(
                    playerStats?.statistics?.["play_one_minute"]?.valueOf() ?? 0
                  )}
                </strong>
                <p className=" text-text-uppercase text-opacity-75">Playtime</p>
              </div>
            </Stack>
          </>
        ) : null}
      </header>

      {isLoading && <LoadingSpinner text={`Loading ${name}'s profile...`} />}
      {isError && <ErrorAlert message={error.message} />}

      {isLoading || isError ? null : (
        <>
          <Table>
            <thead>
              <tr>
                <th>Statistic</th>
                <th>Value</th>
              </tr>
            </thead>
            <tbody>
              {playerStats ? (
                Object.entries(playerStats?.statistics ?? {}).map(
                  ([key, value]) => (
                    <tr key={key}>
                      <td>{key}</td>
                      <td>{value?.toLocaleString()}</td>
                    </tr>
                  )
                )
              ) : (
                <tr>
                  <td colSpan={2}>No statistics available</td>
                </tr>
              )}
            </tbody>
          </Table>
          <Table>
            <thead>
              <tr>
                <th>Block</th>
                <th>Mined</th>
              </tr>
            </thead>
            <tbody>
              {playerStats ? (
                Object.entries(playerStats?.mined_blocks ?? {})
                  .sort(([, a], [, b]) => b - a)
                  .map(([key, value]) => (
                    <tr key={key}>
                      <td>{key}</td>
                      <td>{value?.toLocaleString()}</td>
                    </tr>
                  ))
              ) : (
                <tr>
                  <td colSpan={2}>No statistics available</td>
                </tr>
              )}
            </tbody>
          </Table>
        </>
      )}
    </>
  );
}
