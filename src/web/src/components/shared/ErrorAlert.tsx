import { Alert, type AlertProps } from "react-bootstrap";

type ErrorAlertProps = { message: string } & AlertProps;

const ErrorAlert = ({ message, ...props }: ErrorAlertProps) => {
  return (
    <Alert {...props} variant={props.variant || "danger"}>
      <Alert.Heading className="fw-bolder">Oh snap!</Alert.Heading>
      <p>Something went wrong! Please refresh the page and try again.</p>
      <hr />
      <p className="small font-monospace">Error: {message}</p>
    </Alert>
  );
};

export default ErrorAlert;
