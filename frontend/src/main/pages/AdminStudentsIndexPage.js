import BasicLayout from "main/layouts/BasicLayout/BasicLayout";

export default function AdminStudentsIndexPage() {
  // Stryker disable all : placeholder for future implementation
  return (
    <BasicLayout>
      <div className="pt-2">
        <h1>Index page not yet implemented</h1>
        <p>
          <a href="/admin/students/create">Create</a>
        </p>
        <p>
          <a href="/admin/students/edit/1">Edit</a>
        </p>
      </div>
    </BasicLayout>
  );
}