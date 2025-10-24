import { createFileRoute } from "@tanstack/react-router";
import "../assets/App.css";

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
