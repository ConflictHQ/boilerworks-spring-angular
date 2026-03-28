import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { HeaderComponent } from '../../shared/components/header/header.component';
import { WorkflowService } from '../../core/services/workflow.service';
import { AuthService } from '../../core/services/auth.service';
import { WorkflowDefinition } from '../../core/models/workflow.model';

@Component({
  selector: 'app-workflow-list',
  standalone: true,
  imports: [CommonModule, RouterLink, HeaderComponent],
  template: `
    <app-header title="Workflows"></app-header>
    <div class="page-content">
      <div class="table-container">
        <table class="data-table">
          <thead>
            <tr>
              <th>Name</th>
              <th>States</th>
              <th>Version</th>
              <th>Active</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            @for (wf of workflows(); track wf.id) {
              <tr>
                <td>{{ wf.name }}</td>
                <td>{{ wf.stateMachine?.states?.length || 0 }} states</td>
                <td>v{{ wf.version }}</td>
                <td>
                  <span [class]="wf.active ? 'badge badge-success' : 'badge badge-muted'">
                    {{ wf.active ? 'Active' : 'Inactive' }}
                  </span>
                </td>
                <td class="actions">
                  <a [routerLink]="['/workflows', wf.id]" class="btn btn-sm">View</a>
                </td>
              </tr>
            }
            @if (workflows().length === 0) {
              <tr>
                <td colspan="5" class="empty-state">No workflows found</td>
              </tr>
            }
          </tbody>
        </table>
      </div>
    </div>
  `,
  styles: [
    `
      .page-content {
        padding: 24px 32px;
      }
    `,
  ],
})
export class WorkflowListComponent implements OnInit {
  private workflowService = inject(WorkflowService);
  auth = inject(AuthService);

  workflows = signal<WorkflowDefinition[]>([]);

  ngOnInit() {
    this.workflowService.listDefinitions().subscribe((wfs) => this.workflows.set(wfs));
  }
}
