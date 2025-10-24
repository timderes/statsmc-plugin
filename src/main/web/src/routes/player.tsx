import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/player")({
  component: PlayerPage,
  notFoundComponent: () => {
    return <>NOT FOUND!</>;
  },
});

function PlayerPage() {
  return <Player />;
}

function Player() {
  return <div className="App">PLAYER</div>;
}

export default PlayerPage;
